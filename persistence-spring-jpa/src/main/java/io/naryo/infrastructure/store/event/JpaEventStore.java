package io.naryo.infrastructure.store.event;

import io.naryo.application.store.event.EventStore;
import io.naryo.domain.configuration.store.active.JpaActiveStoreConfiguration;
import io.naryo.domain.event.Event;
import io.naryo.infrastructure.store.JpaStore;

public abstract class JpaEventStore<K, D extends Event<?>> extends JpaStore<K, D>
        implements EventStore<JpaActiveStoreConfiguration, K, D> {}
