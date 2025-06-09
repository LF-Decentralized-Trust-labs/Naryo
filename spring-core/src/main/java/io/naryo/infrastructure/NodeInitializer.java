package io.naryo.infrastructure;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import io.naryo.application.broadcaster.BroadcasterProducer;
import io.naryo.application.event.decoder.ContractEventParameterDecoder;
import io.naryo.application.filter.block.NodeSynchronizer;
import io.naryo.application.node.DefaultNodeRunner;
import io.naryo.application.node.calculator.StartBlockCalculator;
import io.naryo.application.node.dispatch.block.BlockDispatcher;
import io.naryo.application.node.helper.ContractEventDispatcherHelper;
import io.naryo.application.node.interactor.block.factory.BlockInteractorFactory;
import io.naryo.application.node.routing.EventRoutingService;
import io.naryo.application.node.subscription.block.factory.BlockSubscriberFactory;
import io.naryo.application.node.trigger.Trigger;
import io.naryo.application.node.trigger.permanent.EventBroadcasterPermanentTrigger;
import io.naryo.application.node.trigger.permanent.EventStoreBroadcasterPermanentTrigger;
import io.naryo.application.node.trigger.permanent.block.BlockProcessorPermanentTrigger;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterType;
import io.naryo.domain.filter.event.EventFilter;
import io.naryo.domain.node.Node;
import io.naryo.infrastructure.configuration.ConfigurationFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NodeInitializer implements InitializingBean {

    private final List<Trigger<?>> sharedTriggers;
    private final BlockInteractorFactory interactorFactory;
    private final BlockSubscriberFactory subscriberFactory;
    private final ContractEventParameterDecoder decoder;
    private final ConfigurationFacade config;

    public NodeInitializer(
            ConfigurationFacade config,
            BlockInteractorFactory interactorFactory,
            BlockSubscriberFactory subscriberFactory,
            ContractEventParameterDecoder decoder,
            List<BroadcasterProducer> producers) {
        this.config = config;
        this.interactorFactory = interactorFactory;
        this.subscriberFactory = subscriberFactory;
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

    @Override
    public void afterPropertiesSet() {
        var nodes = config.getNodes();
        var filters = config.getFilters();

        for (var node : nodes) {
            try {
                initNode(node, filters);
            } catch (IOException e) {
                log.info("Failed to initialize node {}: {}", node.getId(), e.getMessage());
            }
        }
    }

    private void initNode(Node node, List<Filter> allFilters) throws IOException {
        var interactor = interactorFactory.create(node);
        var nodeFilters = filterForNode(allFilters, node.getId());
        var dispatcher = new BlockDispatcher(new HashSet<>(sharedTriggers));
        var helper = new ContractEventDispatcherHelper(dispatcher, interactor);

        var blockTrigger =
                new BlockProcessorPermanentTrigger(
                        node, eventFilters(nodeFilters), interactor, decoder, helper);
        dispatcher.addTrigger(blockTrigger);

        var subscriber = subscriberFactory.create(interactor, dispatcher, node);
        var synchronizer =
                new NodeSynchronizer(
                        node,
                        new StartBlockCalculator(node, interactor),
                        interactor,
                        nodeFilters,
                        decoder,
                        helper);
        new DefaultNodeRunner(node, subscriber, synchronizer).run();
    }

    private List<Filter> filterForNode(List<Filter> filters, UUID id) {
        return filters.stream().filter(f -> f.getNodeId().equals(id)).toList();
    }

    private List<EventFilter> eventFilters(List<Filter> filters) {
        return filters.stream()
                .filter(f -> f.getType() == FilterType.EVENT)
                .map(EventFilter.class::cast)
                .toList();
    }
}
