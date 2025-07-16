package io.naryo.infrastructure.configuration.persistence.document.node.subscription.block;

import java.math.BigInteger;

import io.naryo.infrastructure.configuration.persistence.document.node.subscription.SubscriptionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.subscription.block.method.BlockSubscriptionMethodDocument;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("block_subscription_properties")
public class BlockSubscriptionPropertiesDocument extends SubscriptionPropertiesDocument {

    private static final BigInteger DEFAULT_INITIAL_BLOCK = BigInteger.valueOf(-1);
    private static final BigInteger DEFAULT_CONFIRMATION_BLOCKS = BigInteger.valueOf(12);
    private static final BigInteger DEFAULT_MISSING_TX_RETRY_BLOCKS = BigInteger.valueOf(200);
    private static final BigInteger DEFAULT_EVENT_INVALIDATION_BLOCK_THRESHOLD = BigInteger.TWO;
    private static final BigInteger DEFAULT_REPLAY_BLOCK_OFFSET = BigInteger.valueOf(12);
    private static final BigInteger DEFAULT_SYNC_BLOCK_LIMIT = BigInteger.valueOf(20000);

    @Getter private @NotNull BlockSubscriptionMethodDocument method;
    private @NotNull BigInteger initialBlock;
    private @NotNull BigInteger confirmationBlocks;
    private @NotNull BigInteger missingTxRetryBlocks;
    private @NotNull BigInteger eventInvalidationBlockThreshold;
    private @NotNull BigInteger replayBlockOffset;
    private @NotNull BigInteger syncBlockLimit;

    public BigInteger getInitialBlock() {
        return this.initialBlock != null ? this.initialBlock : DEFAULT_INITIAL_BLOCK;
    }

    public BigInteger getConfirmationBlocks() {
        return this.confirmationBlocks != null
                ? this.confirmationBlocks
                : DEFAULT_CONFIRMATION_BLOCKS;
    }

    public BigInteger getMissingTxRetryBlocks() {
        return this.missingTxRetryBlocks != null
                ? this.missingTxRetryBlocks
                : DEFAULT_MISSING_TX_RETRY_BLOCKS;
    }

    public BigInteger getEventInvalidationBlockThreshold() {
        return this.eventInvalidationBlockThreshold != null
                ? this.eventInvalidationBlockThreshold
                : DEFAULT_EVENT_INVALIDATION_BLOCK_THRESHOLD;
    }

    public BigInteger getReplayBlockOffset() {
        return this.replayBlockOffset != null
                ? this.replayBlockOffset
                : DEFAULT_REPLAY_BLOCK_OFFSET;
    }

    public BigInteger getSyncBlockLimit() {
        return this.syncBlockLimit != null ? this.syncBlockLimit : DEFAULT_SYNC_BLOCK_LIMIT;
    }
}
