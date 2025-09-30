package io.naryo.application.node;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.naryo.application.broadcaster.BroadcasterProducer;
import io.naryo.application.configuration.resilence.ResilienceRegistry;
import io.naryo.application.configuration.revision.manager.ConfigurationRevisionManager;
import io.naryo.application.configuration.revision.manager.ConfigurationRevisionManagers;
import io.naryo.application.event.decoder.ContractEventParameterDecoder;
import io.naryo.application.filter.block.NodeSynchronizer;
import io.naryo.application.node.calculator.EventStoreStartBlockCalculator;
import io.naryo.application.node.calculator.StartBlockCalculator;
import io.naryo.application.node.dispatch.block.EventDispatcher;
import io.naryo.application.node.helper.ContractEventDispatcherHelper;
import io.naryo.application.node.helper.TransactionEventDispatcherHelper;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.interactor.block.factory.BlockInteractorFactory;
import io.naryo.application.node.routing.EventRoutingService;
import io.naryo.application.node.subscription.block.factory.BlockSubscriberFactory;
import io.naryo.application.node.trigger.Trigger;
import io.naryo.application.node.trigger.permanent.EventBroadcasterPermanentTrigger;
import io.naryo.application.node.trigger.permanent.EventStoreBroadcasterPermanentTrigger;
import io.naryo.application.node.trigger.permanent.block.ProcessorTriggerFactory;
import io.naryo.application.store.Store;
import io.naryo.application.store.event.EventStore;
import io.naryo.application.store.event.block.BlockEventStore;
import io.naryo.application.store.filter.FilterStore;
import io.naryo.application.store.filter.model.FilterState;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.StoreState;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterType;
import io.naryo.domain.filter.event.EventFilter;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.naryo.domain.node.Node;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NodeInitializer {

    private final ConfigurationRevisionManagers configurationRevisionManagers;
    private final Collection<Trigger<?>> sharedTriggers;
    private final ResilienceRegistry resilienceRegistry;
    private final BlockInteractorFactory interactorFactory;
    private final BlockSubscriberFactory subscriberFactory;
    private final ProcessorTriggerFactory processorFactory;
    private final ContractEventParameterDecoder decoder;
    private final Set<Store<?, ?, ?>> stores;

    public NodeInitializer(
            ConfigurationRevisionManagers configurationRevisionManagers,
            ResilienceRegistry resilienceRegistry,
            BlockInteractorFactory interactorFactory,
            BlockSubscriberFactory subscriberFactory,
            ProcessorTriggerFactory processorFactory,
            ContractEventParameterDecoder decoder,
            List<BroadcasterProducer> producers,
            Set<Store<?, ?, ?>> stores) {
        this.configurationRevisionManagers = configurationRevisionManagers;
        this.resilienceRegistry = resilienceRegistry;
        this.interactorFactory = interactorFactory;
        this.subscriberFactory = subscriberFactory;
        this.processorFactory = processorFactory;
        this.decoder = decoder;
        this.stores = stores;

        Collection<Broadcaster> broadcasters =
                getDomainItems(configurationRevisionManagers.broadcasters());
        Collection<Filter> filters = getDomainItems(configurationRevisionManagers.filters());
        Collection<BroadcasterConfiguration> broadcasterConfigurations =
                getDomainItems(configurationRevisionManagers.broadcasterConfigurations());

        this.sharedTriggers =
                List.of(
                        new EventBroadcasterPermanentTrigger(
                                broadcasters,
                                new EventRoutingService(filters),
                                producers,
                                broadcasterConfigurations));
    }

    public Collection<NodeRunner> initialize() {
        Collection<Node> nodes = getDomainItems(configurationRevisionManagers.nodes());
        Collection<Filter> filters = getDomainItems(configurationRevisionManagers.filters());

        return nodes.stream()
                .map(
                        node -> {
                            try {
                                return initNode(node, filters);
                            } catch (IOException e) {
                                log.info(
                                        "Failed to initialize node {}: {}",
                                        node.getId(),
                                        e.getMessage());
                            }
                            return null;
                        })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private NodeRunner initNode(Node node, Collection<Filter> allFilters) throws IOException {
        var interactor = interactorFactory.create(node);
        var storeConfiguration = filterForNode(node);
        var nodeFilters = filterForNode(allFilters, node.getId());
        var dispatcher = new EventDispatcher(resilienceRegistry, new HashSet<>(sharedTriggers));
        var contractEventHelper = new ContractEventDispatcherHelper(dispatcher, interactor);
        var transactionEventHelper = new TransactionEventDispatcherHelper(dispatcher, interactor);

        storeForNode(EventStore.class, BlockEvent.class, storeConfiguration)
                .ifPresent(
                        store ->
                                dispatcher.addTrigger(
                                        new EventStoreBroadcasterPermanentTrigger(
                                                Set.of(store), List.of(storeConfiguration))));

        var blockTrigger =
                processorFactory.createBlockTrigger(
                        node, findEventFilters(nodeFilters), interactor, contractEventHelper);
        dispatcher.addTrigger(blockTrigger);

        var transactionTrigger =
                processorFactory.createTransactionTrigger(
                        node,
                        findTransactionFilters(nodeFilters),
                        interactor,
                        transactionEventHelper);
        dispatcher.addTrigger(transactionTrigger);

        StartBlockCalculator calculator =
                createStartBlockCalculator(node, interactor, storeConfiguration);

        var subscriber = subscriberFactory.create(interactor, dispatcher, node, calculator);
        var synchronizer =
                new NodeSynchronizer(
                        node,
                        calculator,
                        interactor,
                        nodeFilters,
                        decoder,
                        contractEventHelper,
                        resilienceRegistry,
                        storeForNode(FilterStore.class, FilterState.class, storeConfiguration)
                                .orElse(null),
                        storeConfiguration);
        return new DefaultNodeRunner(node, subscriber, synchronizer);
    }

    private <S extends Store<?, ?, ?>> Optional<S> storeForNode(
            Class<S> storeClass, Class<?> dataClass, StoreConfiguration configuration) {
        List<S> eventStores =
                stores.stream()
                        .filter(store -> storeClass.isAssignableFrom(store.getClass()))
                        .map(store -> (S) store)
                        .toList();
        if (configuration.getState().equals(StoreState.ACTIVE) && !eventStores.isEmpty()) {
            ActiveStoreConfiguration storeConfiguration =
                    ((ActiveStoreConfiguration) configuration);
            return Optional.of(
                    eventStores.stream()
                            .filter(
                                    store ->
                                            store.supports(storeConfiguration.getType(), dataClass))
                            .findFirst()
                            .orElseThrow(
                                    () -> new IllegalArgumentException("No event store founded")));
        }
        return Optional.empty();
    }

    private StoreConfiguration filterForNode(Node node) {
        return getDomainItems(configurationRevisionManagers.stores()).stream()
                .filter(configuration -> configuration.getNodeId().equals(node.getId()))
                .findFirst()
                .orElseThrow(
                        () ->
                                new IllegalStateException(
                                        "No event store configuration found for node: "
                                                + node.getId()));
    }

    private List<Filter> filterForNode(Collection<Filter> filters, UUID id) {
        return filters.stream().filter(f -> f.getNodeId().equals(id)).toList();
    }

    private List<TransactionFilter> findTransactionFilters(List<Filter> filters) {
        return findFilters(filters, FilterType.TRANSACTION)
                .map(TransactionFilter.class::cast)
                .toList();
    }

    private List<EventFilter> findEventFilters(List<Filter> filters) {
        return findFilters(filters, FilterType.EVENT).map(EventFilter.class::cast).toList();
    }

    private Stream<Filter> findFilters(List<Filter> filters, FilterType type) {
        return filters.stream().filter(f -> f.getType() == type);
    }

    private StartBlockCalculator createStartBlockCalculator(
            Node node, BlockInteractor interactor, StoreConfiguration configuration) {
        var eventStore = storeForNode(BlockEventStore.class, BlockEvent.class, configuration);
        if (eventStore.isPresent()) {
            return new EventStoreStartBlockCalculator(
                    node, interactor, eventStore.get(), (ActiveStoreConfiguration) configuration);
        }
        return new StartBlockCalculator(node, interactor);
    }

    private <T> Collection<T> getDomainItems(
            ConfigurationRevisionManager<T> configurationRevisionManager) {
        return configurationRevisionManager.liveView().revision().domainItems();
    }
}
