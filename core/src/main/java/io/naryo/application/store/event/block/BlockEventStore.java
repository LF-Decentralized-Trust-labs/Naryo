package io.naryo.application.store.event.block;

import java.math.BigInteger;

import io.naryo.application.store.event.EventStore;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.event.block.BlockEvent;

public interface BlockEventStore<C extends ActiveStoreConfiguration>
        extends EventStore<C, BigInteger, BlockEvent> {}
