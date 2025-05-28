package io.librevents.infrastructure.configuration.source.env.model.node.subscription.block;

import java.math.BigInteger;

import io.librevents.infrastructure.configuration.source.env.model.node.subscription.SubscriptionConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.subscription.block.method.BlockSubscriptionMethodProperties;

public record BlockSubscriptionConfigurationProperties(
        BlockSubscriptionMethodProperties method,
        BigInteger initialBlock,
        BigInteger confirmationBlocks,
        BigInteger missingTxRetryBlocks,
        BigInteger eventInvalidationBlockThreshold,
        BigInteger replayBlockOffset,
        BigInteger syncBlockLimit)
        implements SubscriptionConfigurationProperties {}
