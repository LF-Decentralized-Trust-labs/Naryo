package io.naryo.infrastructure.configuration.source.env.model.node.subscription.block;

import java.math.BigInteger;
import java.util.Optional;

import io.naryo.application.configuration.source.model.node.subscription.BlockSubscriptionDescriptor;
import io.naryo.domain.node.subscription.SubscriptionStrategy;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;
import io.naryo.infrastructure.configuration.source.env.model.node.subscription.SubscriptionProperties;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public abstract class BlockSubscriptionProperties extends SubscriptionProperties
        implements BlockSubscriptionDescriptor {

    private static final BigInteger DEFAULT_INITIAL_BLOCK = BigInteger.valueOf(-1);
    private static final BigInteger DEFAULT_CONFIRMATION_BLOCKS = BigInteger.valueOf(12);
    private static final BigInteger DEFAULT_MISSING_TX_RETRY_BLOCKS = BigInteger.valueOf(200);
    private static final BigInteger DEFAULT_EVENT_INVALIDATION_BLOCK_THRESHOLD = BigInteger.TWO;
    private static final BigInteger DEFAULT_REPLAY_BLOCK_OFFSET = BigInteger.valueOf(12);
    private static final BigInteger DEFAULT_SYNC_BLOCK_LIMIT = BigInteger.valueOf(20000);

    private final @Getter @NotNull BlockSubscriptionMethod method;
    private @Setter @Nullable BigInteger initialBlock;
    private @Setter @Nullable BigInteger confirmationBlocks;
    private @Setter @Nullable BigInteger missingTxRetryBlocks;
    private @Setter @Nullable BigInteger eventInvalidationBlockThreshold;
    private @Setter @Nullable BigInteger replayBlockOffset;
    private @Setter @Nullable BigInteger syncBlockLimit;

    public BlockSubscriptionProperties(
            BlockSubscriptionMethod method,
            BigInteger initialBlock,
            BigInteger confirmationBlocks,
            BigInteger missingTxRetryBlocks,
            BigInteger eventInvalidationBlockThreshold,
            BigInteger replayBlockOffset,
            BigInteger syncBlockLimit) {
        super(SubscriptionStrategy.BLOCK_BASED);
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

    @Override
    public Optional<BigInteger> getInitialBlock() {
        return Optional.ofNullable(initialBlock);
    }

    @Override
    public Optional<BigInteger> getConfirmationBlocks() {
        return Optional.ofNullable(confirmationBlocks);
    }

    @Override
    public Optional<BigInteger> getMissingTxRetryBlocks() {
        return Optional.ofNullable(missingTxRetryBlocks);
    }

    @Override
    public Optional<BigInteger> getEventInvalidationBlockThreshold() {
        return Optional.ofNullable(eventInvalidationBlockThreshold);
    }

    @Override
    public Optional<BigInteger> getReplayBlockOffset() {
        return Optional.ofNullable(replayBlockOffset);
    }

    @Override
    public Optional<BigInteger> getSyncBlockLimit() {
        return Optional.ofNullable(syncBlockLimit);
    }
}
