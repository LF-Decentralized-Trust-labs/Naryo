package io.naryo.application.event.store.block;

import io.naryo.application.event.store.EventStore;
import io.naryo.domain.configuration.eventstore.active.block.BlockEventStoreConfiguration;
import io.naryo.domain.event.contract.ContractEvent;

public interface ContractEventEventStore<C extends BlockEventStoreConfiguration>
        extends EventStore<ContractEvent, C> {}
