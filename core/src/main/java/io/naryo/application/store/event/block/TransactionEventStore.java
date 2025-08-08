package io.naryo.application.store.event.block;

import io.naryo.application.store.event.EventStore;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.event.transaction.TransactionEvent;

public interface TransactionEventStore<C extends ActiveStoreConfiguration>
        extends EventStore<C, String, TransactionEvent> {}
