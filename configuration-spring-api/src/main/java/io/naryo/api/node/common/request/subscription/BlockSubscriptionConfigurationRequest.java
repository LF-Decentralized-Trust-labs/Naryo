package io.naryo.api.node.common.request.subscription;

import java.math.BigInteger;

import io.naryo.api.node.common.request.subscription.method.BlockSubscriptionMethodConfigurationRequest;
import io.naryo.application.configuration.source.model.node.subscription.factory.DefaultBlockSubscriptionFactory;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Schema(description = "Block subscription")
@Getter
public final class BlockSubscriptionConfigurationRequest extends SubscriptionConfigurationRequest {

    @NotNull @Valid private final BlockSubscriptionMethodConfigurationRequest method;

    @Schema(defaultValue = "-1", description = "Initial block to sync from; -1 means latest")
    private final BigInteger initialBlock;

    @PositiveOrZero
    @Schema(defaultValue = "12")
    private final BigInteger confirmationBlocks;

    @PositiveOrZero
    @Schema(defaultValue = "200")
    private final BigInteger missingTxRetryBlocks;

    @PositiveOrZero
    @Schema(defaultValue = "2")
    private final BigInteger eventInvalidationBlockThreshold;

    @PositiveOrZero
    @Schema(defaultValue = "12")
    private final BigInteger replayBlockOffset;

    @PositiveOrZero
    @Schema(defaultValue = "20000")
    private final BigInteger syncBlockLimit;

    public BlockSubscriptionConfigurationRequest(
            BlockSubscriptionMethodConfigurationRequest method,
            BigInteger initialBlock,
            BigInteger confirmationBlocks,
            BigInteger missingTxRetryBlocks,
            BigInteger eventInvalidationBlockThreshold,
            BigInteger replayBlockOffset,
            BigInteger syncBlockLimit) {
        this.method = method;
        this.initialBlock = initialBlock;
        this.confirmationBlocks = confirmationBlocks;
        this.missingTxRetryBlocks = missingTxRetryBlocks;
        this.eventInvalidationBlockThreshold = eventInvalidationBlockThreshold;
        this.replayBlockOffset = replayBlockOffset;
        this.syncBlockLimit = syncBlockLimit;
    }

    @Override
    public SubscriptionConfiguration toDomain() {
        return new BlockSubscriptionConfiguration(
                this.method.toDomain(),
                initialBlock != null
                        ? initialBlock
                        : DefaultBlockSubscriptionFactory.DEFAULT_INITIAL_BLOCK,
                new NonNegativeBlockNumber(
                        confirmationBlocks != null
                                ? confirmationBlocks
                                : DefaultBlockSubscriptionFactory.DEFAULT_CONFIRMATION_BLOCKS),
                new NonNegativeBlockNumber(
                        missingTxRetryBlocks != null
                                ? missingTxRetryBlocks
                                : DefaultBlockSubscriptionFactory.DEFAULT_MISSING_TX_RETRY_BLOCKS),
                new NonNegativeBlockNumber(
                        eventInvalidationBlockThreshold != null
                                ? eventInvalidationBlockThreshold
                                : DefaultBlockSubscriptionFactory
                                        .DEFAULT_EVENT_INVALIDATION_BLOCK_THRESHOLD),
                new NonNegativeBlockNumber(
                        replayBlockOffset != null
                                ? replayBlockOffset
                                : DefaultBlockSubscriptionFactory.DEFAULT_REPLAY_BLOCK_OFFSET),
                new NonNegativeBlockNumber(
                        syncBlockLimit != null
                                ? syncBlockLimit
                                : DefaultBlockSubscriptionFactory.DEFAULT_SYNC_BLOCK_LIMIT));
    }
}
