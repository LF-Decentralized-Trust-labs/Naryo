package io.librevents.infrastructure.configuration.source.env.model.node.subscription.block;

import java.math.BigInteger;

import io.librevents.infrastructure.configuration.source.env.model.node.subscription.SubscriptionConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.subscription.block.method.BlockSubscriptionMethodProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record BlockSubscriptionConfigurationProperties(
        @Valid @NotNull BlockSubscriptionMethodProperties method,
        @NotNull BigInteger initialBlock,
        @NotNull BigInteger confirmationBlocks,
        @NotNull BigInteger missingTxRetryBlocks,
        @NotNull BigInteger eventInvalidationBlockThreshold,
        @NotNull BigInteger replayBlockOffset,
        @NotNull BigInteger syncBlockLimit)
        implements SubscriptionConfigurationProperties {

    private static final BigInteger DEFAULT_INITIAL_BLOCK = BigInteger.ZERO;
    private static final BigInteger DEFAULT_CONFIRMATION_BLOCKS = BigInteger.valueOf(12);
    private static final BigInteger DEFAULT_MISSING_TX_RETRY_BLOCKS = BigInteger.valueOf(200);
    private static final BigInteger DEFAULT_EVENT_INVALIDATION_BLOCK_THRESHOLD = BigInteger.TWO;
    private static final BigInteger DEFAULT_REPLAY_BLOCK_OFFSET = BigInteger.valueOf(12);
    private static final BigInteger DEFAULT_SYNC_BLOCK_LIMIT = BigInteger.valueOf(20000);

    public BlockSubscriptionConfigurationProperties(
            BlockSubscriptionMethodProperties method,
            BigInteger initialBlock,
            BigInteger confirmationBlocks,
            BigInteger missingTxRetryBlocks,
            BigInteger eventInvalidationBlockThreshold,
            BigInteger replayBlockOffset,
            BigInteger syncBlockLimit) {
        this.method = method;
        this.initialBlock = initialBlock != null ? initialBlock : DEFAULT_INITIAL_BLOCK;
        this.confirmationBlocks =
                confirmationBlocks != null ? confirmationBlocks : DEFAULT_CONFIRMATION_BLOCKS;
        this.missingTxRetryBlocks =
                missingTxRetryBlocks != null
                        ? missingTxRetryBlocks
                        : DEFAULT_MISSING_TX_RETRY_BLOCKS;
        this.eventInvalidationBlockThreshold =
                eventInvalidationBlockThreshold != null
                        ? eventInvalidationBlockThreshold
                        : DEFAULT_EVENT_INVALIDATION_BLOCK_THRESHOLD;
        this.replayBlockOffset =
                replayBlockOffset != null ? replayBlockOffset : DEFAULT_REPLAY_BLOCK_OFFSET;
        this.syncBlockLimit = syncBlockLimit != null ? syncBlockLimit : DEFAULT_SYNC_BLOCK_LIMIT;
    }

    public BlockSubscriptionConfigurationProperties() {
        this(
                new BlockSubscriptionMethodProperties(),
                DEFAULT_INITIAL_BLOCK,
                DEFAULT_CONFIRMATION_BLOCKS,
                DEFAULT_MISSING_TX_RETRY_BLOCKS,
                DEFAULT_EVENT_INVALIDATION_BLOCK_THRESHOLD,
                DEFAULT_REPLAY_BLOCK_OFFSET,
                DEFAULT_SYNC_BLOCK_LIMIT);
    }
}
