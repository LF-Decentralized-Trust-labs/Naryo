package io.naryo.domain.node.subscription.block;

import java.math.BigInteger;
import java.util.Objects;

import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;
import io.naryo.domain.node.subscription.SubscriptionStrategy;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethodConfiguration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class BlockSubscriptionConfiguration extends SubscriptionConfiguration {

    private final BlockSubscriptionMethodConfiguration methodConfiguration;
    private final BigInteger initialBlock;
    private final NonNegativeBlockNumber confirmationBlocks;
    private final NonNegativeBlockNumber missingTxRetryBlocks;
    private final NonNegativeBlockNumber eventInvalidationBlockThreshold;
    private final NonNegativeBlockNumber replayBlockOffset;
    private final NonNegativeBlockNumber syncBlockLimit;

    public BlockSubscriptionConfiguration(
            BlockSubscriptionMethodConfiguration methodConfiguration,
            BigInteger initialBlock,
            NonNegativeBlockNumber confirmationBlocks,
            NonNegativeBlockNumber missingTxRetryBlocks,
            NonNegativeBlockNumber eventInvalidationBlockThreshold,
            NonNegativeBlockNumber replayBlockOffset,
            NonNegativeBlockNumber syncBlockLimit) {
        super(SubscriptionStrategy.BLOCK_BASED);
        Objects.requireNonNull(
                methodConfiguration, "blockSubscriptionMethodConfiguration cannot be null");
        Objects.requireNonNull(initialBlock, "initialBlock cannot be null");
        Objects.requireNonNull(confirmationBlocks, "confirmationBlocks cannot be null");
        Objects.requireNonNull(missingTxRetryBlocks, "missingTxRetryBlocks cannot be null");
        Objects.requireNonNull(
                eventInvalidationBlockThreshold, "eventInvalidationBlockThreshold cannot be null");
        Objects.requireNonNull(replayBlockOffset, "replayBlockOffset cannot be null");
        Objects.requireNonNull(syncBlockLimit, "syncBlockLimit cannot be null");
        this.confirmationBlocks = confirmationBlocks;
        this.missingTxRetryBlocks = missingTxRetryBlocks;
        this.methodConfiguration = methodConfiguration;
        this.initialBlock = initialBlock;
        this.eventInvalidationBlockThreshold = eventInvalidationBlockThreshold;
        this.replayBlockOffset = replayBlockOffset;
        this.syncBlockLimit = syncBlockLimit;
    }
}
