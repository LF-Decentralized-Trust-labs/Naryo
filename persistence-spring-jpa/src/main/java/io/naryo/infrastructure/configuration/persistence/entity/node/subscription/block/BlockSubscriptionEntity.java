package io.naryo.infrastructure.configuration.persistence.entity.node.subscription.block;

import java.math.BigInteger;
import java.util.Optional;

import io.naryo.application.configuration.source.model.node.subscription.BlockSubscriptionDescriptor;
import io.naryo.infrastructure.configuration.persistence.entity.node.subscription.SubscriptionEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@NoArgsConstructor
public abstract class BlockSubscriptionEntity extends SubscriptionEntity
        implements BlockSubscriptionDescriptor {

    private static final BigInteger DEFAULT_INITIAL_BLOCK = BigInteger.valueOf(-1);
    private static final BigInteger DEFAULT_CONFIRMATION_BLOCKS = BigInteger.valueOf(12);
    private static final BigInteger DEFAULT_MISSING_TX_RETRY_BLOCKS = BigInteger.valueOf(200);
    private static final BigInteger DEFAULT_EVENT_INVALIDATION_BLOCK_THRESHOLD = BigInteger.TWO;
    private static final BigInteger DEFAULT_REPLAY_BLOCK_OFFSET = BigInteger.valueOf(12);
    private static final BigInteger DEFAULT_SYNC_BLOCK_LIMIT = BigInteger.valueOf(20000);

    @Column(name = "initial_block")
    private @Nullable BigInteger initialBlock;

    @Column(name = "confirmation_blocks")
    private @Nullable BigInteger confirmationBlocks;

    @Column(name = "missing_tx_retry_blocks")
    private @Nullable BigInteger missingTxRetryBlocks;

    @Column(name = "event_invalidation_block_threshold")
    private @Nullable BigInteger eventInvalidationBlockThreshold;

    @Column(name = "replay_block_offset")
    private @Nullable BigInteger replayBlockOffset;

    @Column(name = "sync_block_limit")
    private @Nullable BigInteger syncBlockLimit;

    public BlockSubscriptionEntity(
            BigInteger initialBlock,
            BigInteger confirmationBlocks,
            BigInteger missingTxRetryBlocks,
            BigInteger eventInvalidationBlockThreshold,
            BigInteger replayBlockOffset,
            BigInteger syncBlockLimit) {
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
        return Optional.ofNullable(this.initialBlock);
    }

    @Override
    public Optional<BigInteger> getConfirmationBlocks() {
        return Optional.ofNullable(this.confirmationBlocks);
    }

    @Override
    public Optional<BigInteger> getMissingTxRetryBlocks() {
        return Optional.ofNullable(this.missingTxRetryBlocks);
    }

    @Override
    public Optional<BigInteger> getEventInvalidationBlockThreshold() {
        return Optional.ofNullable(this.eventInvalidationBlockThreshold);
    }

    @Override
    public Optional<BigInteger> getReplayBlockOffset() {
        return Optional.ofNullable(this.replayBlockOffset);
    }

    @Override
    public Optional<BigInteger> getSyncBlockLimit() {
        return Optional.ofNullable(this.syncBlockLimit);
    }
}
