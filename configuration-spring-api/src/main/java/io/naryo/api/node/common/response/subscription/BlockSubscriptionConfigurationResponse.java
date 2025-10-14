package io.naryo.api.node.common.response.subscription;

import java.math.BigInteger;

import io.naryo.api.node.common.response.subscription.method.BlockSubscriptionMethodConfigurationResponse;

public class BlockSubscriptionConfigurationResponse extends SubscriptionConfigurationResponse {

    private final BlockSubscriptionMethodConfigurationResponse methodConfiguration;
    private final BigInteger initialBlock;
    private final BigInteger confirmationBlocks;
    private final BigInteger missingTxRetryBlocks;
    private final BigInteger eventInvalidationBlockThreshold;
    private final BigInteger replayBlockOffset;
    private final BigInteger syncBlockLimit;

    public BlockSubscriptionConfigurationResponse(
            String strategy,
            BlockSubscriptionMethodConfigurationResponse method,
            BigInteger initialBlock,
            BigInteger confirmationBlocks,
            BigInteger missingTxRetryBlocks,
            BigInteger eventInvalidationBlockThreshold,
            BigInteger replayBlockOffset,
            BigInteger syncBlockLimit) {
        super(strategy);
        this.methodConfiguration = method;
        this.initialBlock = initialBlock;
        this.confirmationBlocks = confirmationBlocks;
        this.missingTxRetryBlocks = missingTxRetryBlocks;
        this.eventInvalidationBlockThreshold = eventInvalidationBlockThreshold;
        this.replayBlockOffset = replayBlockOffset;
        this.syncBlockLimit = syncBlockLimit;
    }
}
