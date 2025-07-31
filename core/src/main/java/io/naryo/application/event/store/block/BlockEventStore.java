package io.naryo.application.event.store.block;

import java.math.BigInteger;
import java.util.Optional;

import io.naryo.application.event.store.EventStore;
import io.naryo.domain.configuration.eventstore.BlockEventStoreConfiguration;
import io.naryo.domain.event.block.BlockEvent;

public interface BlockEventStore<C extends BlockEventStoreConfiguration>
        extends EventStore<BlockEvent, C> {

    Optional<BigInteger> getLastestBlock(C configuration);
}
