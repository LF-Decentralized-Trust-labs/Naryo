package io.naryo.application.node;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.naryo.application.broadcaster.BroadcasterProducer;
import io.naryo.application.configuration.resilence.ResilienceRegistry;
import io.naryo.application.event.decoder.ContractEventParameterDecoder;
import io.naryo.application.filter.block.NodeSynchronizer;
import io.naryo.application.node.calculator.StartBlockCalculator;
import io.naryo.application.node.dispatch.block.EventDispatcher;
import io.naryo.application.node.helper.ContractEventDispatcherHelper;
import io.naryo.application.node.helper.TransactionEventDispatcherHelper;
import io.naryo.application.node.interactor.block.factory.BlockInteractorFactory;
import io.naryo.application.node.routing.EventRoutingService;
import io.naryo.application.node.subscription.block.factory.BlockSubscriberFactory;
import io.naryo.application.node.trigger.Trigger;
import io.naryo.application.node.trigger.permanent.EventBroadcasterPermanentTrigger;
import io.naryo.application.node.trigger.permanent.EventStoreBroadcasterPermanentTrigger;
import io.naryo.application.node.trigger.permanent.block.ProcessorTriggerFactory;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterType;
import io.naryo.domain.filter.event.EventFilter;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.naryo.domain.node.Node;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NodeInitializer {

    private final List<Trigger<?>> sharedTriggers;
    private final ResilienceRegistry resilienceRegistry;
    private final BlockInteractorFactory interactorFactory;
    private final BlockSubscriberFactory subscriberFactory;
    private final ProcessorTriggerFactory processorFactory;
    private final ContractEventParameterDecoder decoder;
    private final NodeConfigurationFacade config;

    public NodeInitializer(
            NodeConfigurationFacade config,
            ResilienceRegistry resilienceRegistry,
            BlockInteractorFactory interactorFactory,
            BlockSubscriberFactory subscriberFactory,
            ProcessorTriggerFactory processorFactory,
            ContractEventParameterDecoder decoder,
            List<BroadcasterProducer> producers) {
        this.config = config;
        this.resilienceRegistry = resilienceRegistry;
        this.interactorFactory = interactorFactory;
        this.subscriberFactory = subscriberFactory;
        this.processorFactory = processorFactory;
        this.decoder = decoder;

        this.sharedTriggers =
                List.of(
                        new EventBroadcasterPermanentTrigger(
                                config.getBroadcasters(),
                                new EventRoutingService(config.getFilters()),
                                producers,
                                config.getBroadcasterConfigurations()),
                        new EventStoreBroadcasterPermanentTrigger(List.of()));
    }

    public NodeContainer init() {
        var nodes = config.getNodes();
        var filters = config.getFilters();

        return new NodeContainer(
                nodes.stream()
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
                        .collect(Collectors.toSet()));
    }

    private NodeRunner initNode(Node node, List<Filter> allFilters) throws IOException {
        var interactor = interactorFactory.create(node);
        var nodeFilters = filterForNode(allFilters, node.getId());
        var dispatcher = new EventDispatcher(resilienceRegistry, new HashSet<>(sharedTriggers));
        var contractEventHelper = new ContractEventDispatcherHelper(dispatcher, interactor);
        var transactionEventHelper = new TransactionEventDispatcherHelper(dispatcher, interactor);

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

        var subscriber = subscriberFactory.create(interactor, dispatcher, node);
        var synchronizer =
                new NodeSynchronizer(
                        node,
                        new StartBlockCalculator(node, interactor),
                        interactor,
                        nodeFilters,
                        decoder,
                        contractEventHelper,
                        resilienceRegistry);
        return new DefaultNodeRunner(node, subscriber, synchronizer);
    }

    private List<Filter> filterForNode(List<Filter> filters, UUID id) {
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
}
