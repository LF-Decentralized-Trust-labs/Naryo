package io.naryo.application.store.event.block;

import io.naryo.application.store.event.EventStore;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.event.contract.ContractEvent;

public interface ContractEventEventStore<C extends ActiveStoreConfiguration>
        extends EventStore<C, String, ContractEvent> {}
