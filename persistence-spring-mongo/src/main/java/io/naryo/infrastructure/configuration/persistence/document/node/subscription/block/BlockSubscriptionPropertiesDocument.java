package io.naryo.infrastructure.configuration.persistence.document.node.subscription.block;

import java.math.BigInteger;

import io.naryo.application.configuration.source.model.node.subscription.BlockSubscriptionDescriptor;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;
import io.naryo.infrastructure.configuration.persistence.document.node.subscription.SubscriptionPropertiesDocument;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public abstract class BlockSubscriptionPropertiesDocument extends SubscriptionPropertiesDocument
        implements BlockSubscriptionDescriptor {

    private static final BigInteger DEFAULT_INITIAL_BLOCK = BigInteger.valueOf(-1);
    private static final BigInteger DEFAULT_CONFIRMATION_BLOCKS = BigInteger.valueOf(12);
    private static final BigInteger DEFAULT_MISSING_TX_RETRY_BLOCKS = BigInteger.valueOf(200);
    private static final BigInteger DEFAULT_EVENT_INVALIDATION_BLOCK_THRESHOLD = BigInteger.TWO;
    private static final BigInteger DEFAULT_REPLAY_BLOCK_OFFSET = BigInteger.valueOf(12);
    private static final BigInteger DEFAULT_SYNC_BLOCK_LIMIT = BigInteger.valueOf(20000);

    private @Getter @NotNull BlockSubscriptionMethod method;
    private @Setter @NotNull BigInteger initialBlock;
    private @Setter @NotNull BigInteger confirmationBlocks;
    private @Setter @NotNull BigInteger missingTxRetryBlocks;
    private @Setter @NotNull BigInteger eventInvalidationBlockThreshold;
    private @Setter @NotNull BigInteger replayBlockOffset;
    private @Setter @NotNull BigInteger syncBlockLimit;

    @Override
    public BigInteger getInitialBlock() {
        return this.initialBlock != null ? this.initialBlock : DEFAULT_INITIAL_BLOCK;
    }

    @Override
    public BigInteger getConfirmationBlocks() {
        return this.confirmationBlocks != null
                ? this.confirmationBlocks
                : DEFAULT_CONFIRMATION_BLOCKS;
    }

    @Override
    public BigInteger getMissingTxRetryBlocks() {
        return this.missingTxRetryBlocks != null
                ? this.missingTxRetryBlocks
                : DEFAULT_MISSING_TX_RETRY_BLOCKS;
    }

    @Override
    public BigInteger getEventInvalidationBlockThreshold() {
        return this.eventInvalidationBlockThreshold != null
                ? this.eventInvalidationBlockThreshold
                : DEFAULT_EVENT_INVALIDATION_BLOCK_THRESHOLD;
    }

    @Override
    public BigInteger getReplayBlockOffset() {
        return this.replayBlockOffset != null
                ? this.replayBlockOffset
                : DEFAULT_REPLAY_BLOCK_OFFSET;
    }

    @Override
    public BigInteger getSyncBlockLimit() {
        return this.syncBlockLimit != null ? this.syncBlockLimit : DEFAULT_SYNC_BLOCK_LIMIT;
    }
}
