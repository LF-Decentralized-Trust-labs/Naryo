package io.naryo.api.node.getAll.model.subscription.block;

import java.math.BigInteger;

import io.naryo.api.node.getAll.model.subscription.SubscriptionConfigurationResponse;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Block subscription configuration")
@Getter
public final class BlockSubscriptionConfigurationResponse
        extends SubscriptionConfigurationResponse {

    private final BlockSubscriptionMethodConfigurationResponse methodConfiguration;
    private final BigInteger initialBlock;
    private final BigInteger confirmationBlocks;
    private final BigInteger missingTxRetryBlocks;
    private final BigInteger eventInvalidationBlockThreshold;
    private final BigInteger replayBlockOffset;
    private final BigInteger syncBlockLimit;

    public BlockSubscriptionConfigurationResponse(
            BlockSubscriptionMethodConfigurationResponse methodConfiguration,
            BigInteger initialBlock,
            BigInteger confirmationBlocks,
            BigInteger missingTxRetryBlocks,
            BigInteger eventInvalidationBlockThreshold,
            BigInteger replayBlockOffset,
            BigInteger syncBlockLimit) {
        this.methodConfiguration = methodConfiguration;
        this.initialBlock = initialBlock;
        this.confirmationBlocks = confirmationBlocks;
        this.missingTxRetryBlocks = missingTxRetryBlocks;
        this.eventInvalidationBlockThreshold = eventInvalidationBlockThreshold;
        this.replayBlockOffset = replayBlockOffset;
        this.syncBlockLimit = syncBlockLimit;
    }

    public static BlockSubscriptionConfigurationResponse fromDomain(
            BlockSubscriptionConfiguration blockSubscriptionConfiguration) {
        return new BlockSubscriptionConfigurationResponse(
                BlockSubscriptionMethodConfigurationResponse.fromDomain(
                        blockSubscriptionConfiguration.getMethodConfiguration()),
                blockSubscriptionConfiguration.getInitialBlock(),
                blockSubscriptionConfiguration.getConfirmationBlocks().value(),
                blockSubscriptionConfiguration.getMissingTxRetryBlocks().value(),
                blockSubscriptionConfiguration.getEventInvalidationBlockThreshold().value(),
                blockSubscriptionConfiguration.getReplayBlockOffset().value(),
                blockSubscriptionConfiguration.getSyncBlockLimit().value());
    }
}
