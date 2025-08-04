package io.naryo.application.node.calculator;

import java.math.BigInteger;

import io.naryo.application.event.store.block.BlockEventStore;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.domain.configuration.eventstore.active.block.BlockEventStoreConfiguration;
import io.naryo.domain.node.Node;

public final class EventStoreStartBlockCalculator extends StartBlockCalculator {

    private final BlockEventStore<?> blockEventStore;
    private final BlockEventStoreConfiguration blockEventStoreConfiguration;

    public EventStoreStartBlockCalculator(
            Node node,
            BlockInteractor interactor,
            BlockEventStore<?> blockEventStore,
            BlockEventStoreConfiguration blockEventStoreConfiguration) {
        super(node, interactor);
        this.blockEventStore = blockEventStore;
        this.blockEventStoreConfiguration = blockEventStoreConfiguration;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected BigInteger calculateLatestBlock() {
        return ((BlockEventStore<BlockEventStoreConfiguration>) blockEventStore)
                .getLastestBlock(blockEventStoreConfiguration)
                .orElse(BigInteger.valueOf(-1));
    }
}
