package io.naryo.api.node.common.subscription;

import java.math.BigInteger;

import io.naryo.api.node.common.request.subscription.BlockSubscriptionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.method.BlockSubscriptionMethodConfigurationRequest;
import io.naryo.api.node.common.subscription.method.PubSubBlockSubscriptionMethodConfigurationRequestBuilder;
import org.instancio.Instancio;

public final class BlockSubscriptionConfigurationRequestBuilder
        extends SubscriptionConfigurationRequestBuilder<
                BlockSubscriptionConfigurationRequestBuilder,
                BlockSubscriptionConfigurationRequest> {

    private BlockSubscriptionMethodConfigurationRequest methodConfiguration;
    private BigInteger initialBlock;
    private BigInteger confirmationBlocks;
    private BigInteger missingTxRetryBlocks;
    private BigInteger eventInvalidationBlockThreshold;
    private BigInteger replayBlockOffset;
    private BigInteger syncBlockLimit;

    @Override
    public BlockSubscriptionConfigurationRequestBuilder self() {
        return this;
    }

    @Override
    public BlockSubscriptionConfigurationRequest build() {
        return new BlockSubscriptionConfigurationRequest(
                getMethodConfiguration(),
                getInitialBlock(),
                getConfirmationBlocks(),
                getMissingTxRetryBlocks(),
                getEventInvalidationBlockThreshold(),
                getReplayBlockOffset(),
                getSyncBlockLimit());
    }

    public BlockSubscriptionConfigurationRequestBuilder withMethodConfiguration(
            BlockSubscriptionMethodConfigurationRequest methodConfiguration) {
        this.methodConfiguration = methodConfiguration;
        return self();
    }

    public BlockSubscriptionConfigurationRequestBuilder withInitialBlock(BigInteger initialBlock) {
        this.initialBlock = initialBlock;
        return self();
    }

    public BlockSubscriptionConfigurationRequestBuilder withConfirmationBlocks(
            BigInteger confirmationBlocks) {
        this.confirmationBlocks = confirmationBlocks;
        return self();
    }

    public BlockSubscriptionConfigurationRequestBuilder withMissingTxRetryBlocks(
            BigInteger missingTxRetryBlocks) {
        this.missingTxRetryBlocks = missingTxRetryBlocks;
        return self();
    }

    public BlockSubscriptionConfigurationRequestBuilder withEventInvalidationBlockThreshold(
            BigInteger eventInvalidationBlockThreshold) {
        this.eventInvalidationBlockThreshold = eventInvalidationBlockThreshold;
        return self();
    }

    public BlockSubscriptionConfigurationRequestBuilder withReplayBlockOffset(
            BigInteger replayBlockOffset) {
        this.replayBlockOffset = replayBlockOffset;
        return self();
    }

    public BlockSubscriptionConfigurationRequestBuilder withSyncBlockLimit(
            BigInteger syncBlockLimit) {
        this.syncBlockLimit = syncBlockLimit;
        return self();
    }

    public BlockSubscriptionMethodConfigurationRequest getMethodConfiguration() {
        return this.methodConfiguration == null
                ? new PubSubBlockSubscriptionMethodConfigurationRequestBuilder().build()
                : this.methodConfiguration;
    }

    public BigInteger getInitialBlock() {
        return this.initialBlock == null ? Instancio.create(BigInteger.class) : this.initialBlock;
    }

    public BigInteger getConfirmationBlocks() {
        return this.confirmationBlocks == null
                ? Instancio.create(BigInteger.class)
                : this.confirmationBlocks;
    }

    public BigInteger getMissingTxRetryBlocks() {
        return this.missingTxRetryBlocks == null
                ? Instancio.create(BigInteger.class)
                : this.missingTxRetryBlocks;
    }

    public BigInteger getEventInvalidationBlockThreshold() {
        return this.eventInvalidationBlockThreshold == null
                ? Instancio.create(BigInteger.class)
                : this.eventInvalidationBlockThreshold;
    }

    public BigInteger getReplayBlockOffset() {
        return this.replayBlockOffset == null
                ? Instancio.create(BigInteger.class)
                : this.replayBlockOffset;
    }

    public BigInteger getSyncBlockLimit() {
        return this.syncBlockLimit == null
                ? Instancio.create(BigInteger.class)
                : this.syncBlockLimit;
    }
}
