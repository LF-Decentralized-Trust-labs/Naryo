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
        this.initialBlock = initialBlock;
        this.confirmationBlocks = confirmationBlocks;
        this.missingTxRetryBlocks = missingTxRetryBlocks;
        this.eventInvalidationBlockThreshold = eventInvalidationBlockThreshold;
        this.replayBlockOffset = replayBlockOffset;
        this.syncBlockLimit = syncBlockLimit;
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
