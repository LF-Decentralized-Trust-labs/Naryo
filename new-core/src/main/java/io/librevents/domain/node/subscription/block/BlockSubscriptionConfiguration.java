package io.librevents.domain.node.subscription.block;

import java.util.Objects;

import io.librevents.domain.node.subscription.SubscriptionConfiguration;
import io.librevents.domain.node.subscription.SubscriptionStrategy;
import io.librevents.domain.node.subscription.block.method.BlockSubscriptionMethodConfiguration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class BlockSubscriptionConfiguration extends SubscriptionConfiguration {

    private final BlockSubscriptionMethodConfiguration blockSubscriptionMethodConfiguration;
    private final NonNegativeBlockNumber initialBlock;
    private final NonNegativeBlockNumber latestBlock;
    private final NonNegativeBlockNumber confirmationBlocks;
    private final NonNegativeBlockNumber missingTxRetryBlocks;
    private final NonNegativeBlockNumber eventInvalidationBlockThreshold;
    private final NonNegativeBlockNumber replayBlockOffset;
    private final NonNegativeBlockNumber syncBlockLimit;

    public BlockSubscriptionConfiguration(
            BlockSubscriptionMethodConfiguration blockSubscriptionMethodConfiguration,
            NonNegativeBlockNumber initialBlock,
            NonNegativeBlockNumber latestBlock,
            NonNegativeBlockNumber confirmationBlocks,
            NonNegativeBlockNumber missingTxRetryBlocks,
            NonNegativeBlockNumber eventInvalidationBlockThreshold,
            NonNegativeBlockNumber replayBlockOffset,
            NonNegativeBlockNumber syncBlockLimit) {
        super(SubscriptionStrategy.BLOCK_BASED);
        Objects.requireNonNull(
                blockSubscriptionMethodConfiguration,
                "blockSubscriptionMethodConfiguration cannot be null");
        Objects.requireNonNull(initialBlock, "initialBlock cannot be null");
        Objects.requireNonNull(latestBlock, "latestBlock cannot be null");
        Objects.requireNonNull(confirmationBlocks, "confirmationBlocks cannot be null");
        Objects.requireNonNull(missingTxRetryBlocks, "missingTxRetryBlocks cannot be null");
        Objects.requireNonNull(
                eventInvalidationBlockThreshold, "eventInvalidationBlockThreshold cannot be null");
        Objects.requireNonNull(replayBlockOffset, "replayBlockOffset cannot be null");
        Objects.requireNonNull(syncBlockLimit, "syncBlockLimit cannot be null");
        this.confirmationBlocks = confirmationBlocks;
        this.missingTxRetryBlocks = missingTxRetryBlocks;
        this.blockSubscriptionMethodConfiguration = blockSubscriptionMethodConfiguration;
        this.initialBlock = initialBlock;
        this.latestBlock = latestBlock;
        this.eventInvalidationBlockThreshold = eventInvalidationBlockThreshold;
        this.replayBlockOffset = replayBlockOffset;
        this.syncBlockLimit = syncBlockLimit;
    }
}
