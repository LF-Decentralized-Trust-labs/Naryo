package io.librevents.application.node.calculator;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;

import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.domain.node.Node;
import io.librevents.domain.node.subscription.block.BlockSubscriptionConfiguration;

public final class StartBlockCalculator {

    private final Node node;
    private final BlockInteractor interactor;

    public StartBlockCalculator(Node node, BlockInteractor interactor) {
        Objects.requireNonNull(node, "node must not be null");
        Objects.requireNonNull(interactor, "interactor must not be null");
        this.node = node;
        this.interactor = interactor;
    }

    public BigInteger getStartBlock() throws IOException {
        BlockSubscriptionConfiguration configuration =
                (BlockSubscriptionConfiguration) node.getSubscriptionConfiguration();
        if (configuration.getLatestBlock().value().equals(BigInteger.ZERO)) {
            return !configuration.getInitialBlock().isZero()
                    ? configuration.getInitialBlock().value()
                    : interactor.getCurrentBlockNumber();
        }

        BigInteger startBlock = calculateStartBlock(configuration);
        return startBlock.signum() > 0 ? startBlock : BigInteger.ZERO;
    }

    private BigInteger calculateStartBlock(BlockSubscriptionConfiguration configuration)
            throws IOException {
        BigInteger startBlock =
                BigInteger.valueOf(configuration.getLatestBlock().value().intValue())
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
