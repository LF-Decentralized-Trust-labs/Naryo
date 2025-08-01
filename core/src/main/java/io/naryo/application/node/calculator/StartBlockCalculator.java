package io.naryo.application.node.calculator;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;

import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;

public class StartBlockCalculator {

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
        BigInteger latestBlock = calculateLatestBlock();
        if (latestBlock.signum() < 1) {
            return configuration.getInitialBlock().signum() >= 0
                    ? configuration.getInitialBlock()
                    : interactor.getCurrentBlockNumber();
        }
        BigInteger startBlock = calculateStartBlock(configuration, latestBlock);
        return startBlock.signum() > 0 ? startBlock : BigInteger.ZERO;
    }

    protected BigInteger calculateLatestBlock() {
        return BigInteger.valueOf(-1);
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
