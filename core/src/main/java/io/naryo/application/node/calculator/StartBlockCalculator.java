package io.naryo.application.node.calculator;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;

import io.naryo.application.event.store.block.BlockEventStore;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.domain.configuration.eventstore.BlockEventStoreConfiguration;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;
import jakarta.annotation.Nullable;

public final class StartBlockCalculator {

    private final Node node;
    private final BlockInteractor interactor;
    private final @Nullable BlockEventStore<? extends BlockEventStoreConfiguration> blockEventStore;
    private final BlockEventStoreConfiguration blockEventStoreConfiguration;

    public StartBlockCalculator(
            Node node,
            BlockInteractor interactor,
            @Nullable BlockEventStore<?> blockEventStore,
            BlockEventStoreConfiguration blockEventStoreConfiguration) {
        Objects.requireNonNull(node, "node must not be null");
        Objects.requireNonNull(interactor, "interactor must not be null");
        Objects.requireNonNull(
                blockEventStoreConfiguration, "blockEventStoreConfiguration must not be null");
        this.node = node;
        this.interactor = interactor;
        this.blockEventStore = blockEventStore;
        this.blockEventStoreConfiguration = blockEventStoreConfiguration;
    }

    public BigInteger getStartBlock() throws IOException {
        BlockSubscriptionConfiguration configuration =
                (BlockSubscriptionConfiguration) node.getSubscriptionConfiguration();
        BigInteger latestBlock = BigInteger.valueOf(-1);
        if (blockEventStore != null) {
            @SuppressWarnings("unchecked")
            BlockEventStore<BlockEventStoreConfiguration> typedEventStore =
                    (BlockEventStore<BlockEventStoreConfiguration>) blockEventStore;
            latestBlock =
                    typedEventStore
                            .getLastestBlock(blockEventStoreConfiguration)
                            .orElse(BigInteger.valueOf(-1));
            if (latestBlock.signum() < 1) {
                return configuration.getInitialBlock().signum() >= 0
                        ? configuration.getInitialBlock()
                        : interactor.getCurrentBlockNumber();
            }
        }

        BigInteger startBlock = calculateStartBlock(configuration, latestBlock);
        return startBlock.signum() > 0 ? startBlock : BigInteger.ZERO;
    }

    private BigInteger calculateStartBlock(
            BlockSubscriptionConfiguration configuration, BigInteger latestBlock)
            throws IOException {
        BigInteger startBlock =
                BigInteger.valueOf(latestBlock.intValue())
                        .subtract(configuration.getReplayBlockOffset().value());

        BigInteger syncBlockLimit = configuration.getSyncBlockLimit().value();
        if (syncBlockLimit.compareTo(BigInteger.ZERO) > 0) {
            BigInteger currentBlock = interactor.getCurrentBlockNumber();
            if (currentBlock.subtract(startBlock).compareTo(syncBlockLimit) > 0) {
                startBlock = currentBlock.subtract(syncBlockLimit);
            }
        }
        return startBlock;
    }
}
