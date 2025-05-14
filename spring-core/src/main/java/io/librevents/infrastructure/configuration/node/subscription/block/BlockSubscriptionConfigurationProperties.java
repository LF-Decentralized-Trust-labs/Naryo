package io.librevents.infrastructure.configuration.node.subscription.block;

import io.librevents.infrastructure.configuration.node.subscription.SubscriptionConfigurationProperties;
import io.librevents.infrastructure.configuration.node.subscription.block.method.BlockSubscriptionMethodProperties;

public record BlockSubscriptionConfigurationProperties(
        BlockSubscriptionMethodProperties method,
        Integer initialBlock,
        Integer confirmationBlocks,
        Integer missingTxRetryBlocks,
        Integer eventInvalidationBlockThreshold,
        Integer replayBlockOffset,
        Integer syncBlockLimit)
        implements SubscriptionConfigurationProperties {}
