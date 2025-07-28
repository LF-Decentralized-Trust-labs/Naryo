package io.naryo.application.node.subscription.block.factory;

import java.util.Optional;
import java.util.Set;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.naryo.application.common.Mapper;
import io.naryo.application.configuration.resilence.ResilienceRegistry;
import io.naryo.application.event.store.EventStore;
import io.naryo.application.event.store.block.BlockEventStore;
import io.naryo.application.node.calculator.StartBlockCalculator;
import io.naryo.application.node.dispatch.Dispatcher;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.interactor.block.dto.Block;
import io.naryo.application.node.subscription.block.BlockSubscriber;
import io.naryo.application.node.subscription.block.poll.PollBlockSubscriber;
import io.naryo.application.node.subscription.block.pubsub.PubSubBlockSubscriber;
import io.naryo.domain.configuration.eventstore.BlockEventStoreConfiguration;
import io.naryo.domain.configuration.eventstore.EventStoreConfiguration;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.subscription.SubscriptionStrategy;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;

public final class DefaultBlockSubscriberFactory implements BlockSubscriberFactory {

    private final Mapper<Block, BlockEvent> blockMapper;
    private final ResilienceRegistry resilienceRegistry;

    public DefaultBlockSubscriberFactory(
            Mapper<Block, BlockEvent> blockMapper, ResilienceRegistry resilienceRegistry) {
        this.blockMapper = blockMapper;
        this.resilienceRegistry = resilienceRegistry;
    }

    @Override
    public BlockSubscriber create(
            BlockInteractor interactor,
            Dispatcher dispatcher,
            Node node,
            EventStoreConfiguration configuration,
            Set<EventStore<? extends Event, ? extends EventStoreConfiguration>> eventStores) {
        if (node.getSubscriptionConfiguration()
                .getStrategy()
                .equals(SubscriptionStrategy.BLOCK_BASED)) {
            StartBlockCalculator calculator;
            Optional<EventStore<?, ?>> storeOpt =
                    eventStores.stream()
                            .filter(eventStore -> eventStore instanceof BlockEventStore<?>)
                            .findFirst();
            if (storeOpt.isPresent()
                    && storeOpt.get() instanceof BlockEventStore<?> blockEventStore
                    && configuration
                            instanceof BlockEventStoreConfiguration blockEventStoreConfiguration) {

                calculator =
                        new StartBlockCalculator(
                                node, interactor, blockEventStore, blockEventStoreConfiguration);
            } else {
                throw new IllegalArgumentException(
                        "Unsupported event store for type: "
                                + node.getSubscriptionConfiguration().getStrategy().name());
            }
            return switch (((BlockSubscriptionConfiguration) node.getSubscriptionConfiguration())
                    .getMethodConfiguration()
                    .getMethod()) {
                case POLL ->
                        new PollBlockSubscriber(
                                interactor,
                                dispatcher,
                                node,
                                blockMapper,
                                calculator,
                                resilienceRegistry.getOrDefault(
                                        "subscription-cb", CircuitBreaker.class),
                                resilienceRegistry.getOrDefault("subscription-retry", Retry.class));
                case PUBSUB ->
                        new PubSubBlockSubscriber(
                                interactor,
                                dispatcher,
                                node,
                                blockMapper,
                                calculator,
                                resilienceRegistry.getOrDefault(
                                        "subscription-cb", CircuitBreaker.class),
                                resilienceRegistry.getOrDefault("subscription-retry", Retry.class));
            };
        } else {
            throw new IllegalArgumentException(
                    "Unsupported subscription strategy: "
                            + node.getSubscriptionConfiguration().getStrategy());
        }
    }
}
