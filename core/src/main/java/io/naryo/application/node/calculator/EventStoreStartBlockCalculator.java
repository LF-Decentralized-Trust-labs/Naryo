package io.naryo.application.node.calculator;

import java.math.BigInteger;
import java.util.Optional;

import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.store.event.block.BlockEventStore;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.node.Node;

public final class EventStoreStartBlockCalculator extends StartBlockCalculator {

    private final BlockEventStore<?> eventStore;
    private final ActiveStoreConfiguration storeConfiguration;

    public EventStoreStartBlockCalculator(
            Node node,
            BlockInteractor interactor,
            BlockEventStore<?> eventStore,
            ActiveStoreConfiguration storeConfiguration) {
        super(node, interactor);
        this.eventStore = eventStore;
        this.storeConfiguration = storeConfiguration;
    }

    @Override
    protected BigInteger calculateLatestBlock() {
        @SuppressWarnings("unchecked")
        BlockEventStore<ActiveStoreConfiguration> typedStore =
                (BlockEventStore<ActiveStoreConfiguration>) eventStore;
        Optional<BigInteger> event = typedStore.getLatest(storeConfiguration);
        return event.orElseGet(() -> BigInteger.valueOf(-1));
    }
}
