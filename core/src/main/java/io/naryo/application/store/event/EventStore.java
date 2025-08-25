package io.naryo.application.store.event;

import io.naryo.application.store.Store;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.event.Event;

public interface EventStore<C extends ActiveStoreConfiguration, K, E extends Event>
        extends Store<C, K, E> {}
