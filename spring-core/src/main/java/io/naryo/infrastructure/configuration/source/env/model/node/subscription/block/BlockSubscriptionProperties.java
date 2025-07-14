package io.naryo.infrastructure.configuration.source.env.model.node.subscription.block;

import java.math.BigInteger;

import io.naryo.domain.node.subscription.SubscriptionStrategy;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;
import io.naryo.infrastructure.configuration.source.env.model.node.subscription.SubscriptionProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public abstract class BlockSubscriptionProperties extends SubscriptionProperties {

    private static final BigInteger DEFAULT_INITIAL_BLOCK = BigInteger.valueOf(-1);
    private static final BigInteger DEFAULT_CONFIRMATION_BLOCKS = BigInteger.valueOf(12);
    private static final BigInteger DEFAULT_MISSING_TX_RETRY_BLOCKS = BigInteger.valueOf(200);
    private static final BigInteger DEFAULT_EVENT_INVALIDATION_BLOCK_THRESHOLD = BigInteger.TWO;
    private static final BigInteger DEFAULT_REPLAY_BLOCK_OFFSET = BigInteger.valueOf(12);
    private static final BigInteger DEFAULT_SYNC_BLOCK_LIMIT = BigInteger.valueOf(20000);

    private final @Getter @NotNull BlockSubscriptionMethod method;
    private @Getter @Setter @NotNull BigInteger initialBlock;
    private @Getter @Setter @NotNull BigInteger confirmationBlocks;
    private @Getter @Setter @NotNull BigInteger missingTxRetryBlocks;
    private @Getter @Setter @NotNull BigInteger eventInvalidationBlockThreshold;
    private @Getter @Setter @NotNull BigInteger replayBlockOffset;
    private @Getter @Setter @NotNull BigInteger syncBlockLimit;

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
}
