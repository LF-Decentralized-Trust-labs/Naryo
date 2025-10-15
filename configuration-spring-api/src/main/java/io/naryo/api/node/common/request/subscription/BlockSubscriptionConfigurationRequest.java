package io.naryo.api.node.common.request.subscription;

import java.math.BigInteger;

import io.naryo.api.node.common.request.subscription.method.BlockSubscriptionMethodConfigurationRequest;
import io.naryo.domain.node.subscription.SubscriptionStrategy;
import jakarta.validation.Valid;
import lombok.Getter;

@Getter
public final class BlockSubscriptionConfigurationRequest extends SubscriptionConfigurationRequest {

    private final @Valid BlockSubscriptionMethodConfigurationRequest method;
    private final BigInteger initialBlock;
    private final BigInteger confirmationBlocks;
    private final BigInteger missingTxRetryBlocks;
    private final BigInteger eventInvalidationBlockThreshold;
    private final BigInteger replayBlockOffset;
    private final BigInteger syncBlockLimit;

    BlockSubscriptionConfigurationRequest(
            String strategy,
            BlockSubscriptionMethodConfigurationRequest method,
            BigInteger initialBlock,
            BigInteger confirmationBlocks,
            BigInteger missingTxRetryBlocks,
            BigInteger eventInvalidationBlockThreshold,
            BigInteger replayBlockOffset,
            BigInteger syncBlockLimit) {
        super(SubscriptionStrategy.BLOCK_BASED);
        this.method = method;
        this.initialBlock = initialBlock;
        this.confirmationBlocks = confirmationBlocks;
        this.missingTxRetryBlocks = missingTxRetryBlocks;
        this.eventInvalidationBlockThreshold = eventInvalidationBlockThreshold;
        this.replayBlockOffset = replayBlockOffset;
        this.syncBlockLimit = syncBlockLimit;
    }
}
