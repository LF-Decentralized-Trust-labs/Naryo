package io.naryo.application.event.store.block;

import io.naryo.application.event.store.EventStore;
import io.naryo.domain.configuration.eventstore.active.block.BlockEventStoreConfiguration;
import io.naryo.domain.event.transaction.TransactionEvent;

public interface TransactionEventStore<C extends BlockEventStoreConfiguration>
        extends EventStore<TransactionEvent, C> {}
