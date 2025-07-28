package io.naryo.application.node.subscription.block.factory;

import java.util.Set;

import io.naryo.application.event.store.EventStore;
import io.naryo.application.node.dispatch.Dispatcher;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.subscription.block.BlockSubscriber;
import io.naryo.domain.configuration.eventstore.EventStoreConfiguration;
import io.naryo.domain.event.Event;
import io.naryo.domain.node.Node;

public interface BlockSubscriberFactory {

    BlockSubscriber create(
            BlockInteractor interactor,
            Dispatcher dispatcher,
            Node node,
            EventStoreConfiguration configuration,
            Set<EventStore<? extends Event, ? extends EventStoreConfiguration>> eventStores);
}
