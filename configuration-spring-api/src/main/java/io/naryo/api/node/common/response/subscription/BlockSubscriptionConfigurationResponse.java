package io.naryo.api.node.common.response.subscription;

import java.math.BigInteger;

import io.naryo.api.node.common.response.subscription.method.BlockSubscriptionMethodConfigurationResponse;
import lombok.Builder;

@Builder
public record BlockSubscriptionConfigurationResponse(
        String strategy,
        BlockSubscriptionMethodConfigurationResponse method,
        BigInteger initialBlock,
        BigInteger confirmationBlocks,
        BigInteger missingTxRetryBlocks,
        BigInteger eventInvalidationBlockThreshold,
        BigInteger replayBlockOffset,
        BigInteger syncBlockLimit)
        implements SubscriptionConfigurationResponse {}
