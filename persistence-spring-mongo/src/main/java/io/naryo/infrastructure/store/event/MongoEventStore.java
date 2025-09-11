package io.naryo.infrastructure.store.event;

import io.naryo.application.store.event.EventStore;
import io.naryo.domain.configuration.store.MongoStoreConfiguration;
import io.naryo.domain.event.Event;
import io.naryo.infrastructure.store.MongoStore;

public abstract class MongoEventStore<K, D extends Event<?>> extends MongoStore<K, D>
        implements EventStore<MongoStoreConfiguration, K, D> {}
