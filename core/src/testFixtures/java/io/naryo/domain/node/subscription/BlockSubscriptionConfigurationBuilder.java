package io.naryo.domain.node.subscription;

import java.math.BigInteger;

import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethodConfiguration;
import io.naryo.domain.node.subscription.method.PubSubBlockSubscriptionMethodConfigurationBuilder;
import org.instancio.Instancio;

public final class BlockSubscriptionConfigurationBuilder
        extends SubscriptionConfigurationBuilder<
                BlockSubscriptionConfigurationBuilder, BlockSubscriptionConfiguration> {

    private BlockSubscriptionMethodConfiguration methodConfiguration;
    private BigInteger initialBlock;
    private NonNegativeBlockNumber confirmationBlocks;
    private NonNegativeBlockNumber missingTxRetryBlocks;
    private NonNegativeBlockNumber eventInvalidationBlockThreshold;
    private NonNegativeBlockNumber replayBlockOffset;
    private NonNegativeBlockNumber syncBlockLimit;

    @Override
    public BlockSubscriptionConfigurationBuilder self() {
        return this;
    }

    @Override
    public BlockSubscriptionConfiguration build() {
        return new BlockSubscriptionConfiguration(
                getMethodConfiguration(),
                getInitialBlock(),
                getConfirmationBlocks(),
                getMissingTxRetryBlocks(),
                getEventInvalidationBlockThreshold(),
                getReplayBlockOffset(),
                getSyncBlockLimit());
    }

    public BlockSubscriptionConfigurationBuilder withMethodConfiguration(
            BlockSubscriptionMethodConfiguration methodConfiguration) {
        this.methodConfiguration = methodConfiguration;
        return self();
    }

    public BlockSubscriptionConfigurationBuilder withInitialBlock(BigInteger initialBlock) {
        this.initialBlock = initialBlock;
        return self();
    }

    public BlockSubscriptionConfigurationBuilder withConfirmationBlocks(
            NonNegativeBlockNumber confirmationBlocks) {
        this.confirmationBlocks = confirmationBlocks;
        return self();
    }

    public BlockSubscriptionConfigurationBuilder withMissingTxRetryBlocks(
            NonNegativeBlockNumber missingTxRetryBlocks) {
        this.missingTxRetryBlocks = missingTxRetryBlocks;
        return self();
    }

    public BlockSubscriptionConfigurationBuilder withEventInvalidationBlockThreshold(
            NonNegativeBlockNumber eventInvalidationBlockThreshold) {
        this.eventInvalidationBlockThreshold = eventInvalidationBlockThreshold;
        return self();
    }

    public BlockSubscriptionConfigurationBuilder withReplayBlockOffset(
            NonNegativeBlockNumber replayBlockOffset) {
        this.replayBlockOffset = replayBlockOffset;
        return self();
    }

    public BlockSubscriptionConfigurationBuilder withSyncBlockLimit(
            NonNegativeBlockNumber syncBlockLimit) {
        this.syncBlockLimit = syncBlockLimit;
        return self();
    }

    public BlockSubscriptionMethodConfiguration getMethodConfiguration() {
        return this.methodConfiguration == null
                ? new PubSubBlockSubscriptionMethodConfigurationBuilder().build()
                : this.methodConfiguration;
    }

    public BigInteger getInitialBlock() {
        return this.initialBlock == null ? Instancio.create(BigInteger.class) : this.initialBlock;
    }

    public NonNegativeBlockNumber getConfirmationBlocks() {
        return this.confirmationBlocks == null
                ? Instancio.create(NonNegativeBlockNumber.class)
                : this.confirmationBlocks;
    }

    public NonNegativeBlockNumber getMissingTxRetryBlocks() {
        return this.missingTxRetryBlocks == null
                ? Instancio.create(NonNegativeBlockNumber.class)
                : this.missingTxRetryBlocks;
    }

    public NonNegativeBlockNumber getEventInvalidationBlockThreshold() {
        return this.eventInvalidationBlockThreshold == null
                ? Instancio.create(NonNegativeBlockNumber.class)
                : this.eventInvalidationBlockThreshold;
    }

    public NonNegativeBlockNumber getReplayBlockOffset() {
        return this.replayBlockOffset == null
                ? Instancio.create(NonNegativeBlockNumber.class)
                : this.replayBlockOffset;
    }

    public NonNegativeBlockNumber getSyncBlockLimit() {
        return this.syncBlockLimit == null
                ? Instancio.create(NonNegativeBlockNumber.class)
                : this.syncBlockLimit;
    }
}
