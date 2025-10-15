package io.naryo.api.node.common.response.subscription;

import io.naryo.api.node.common.response.subscription.method.BlockSubscriptionMethodConfigurationResponse;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;

public sealed interface SubscriptionConfigurationResponse
        permits BlockSubscriptionConfigurationResponse {
    static SubscriptionConfigurationResponse fromDomain(
            SubscriptionConfiguration subscriptionConfiguration) {
        var blockSubscription = (BlockSubscriptionConfiguration) subscriptionConfiguration;
        return BlockSubscriptionConfigurationResponse.builder()
                .strategy(subscriptionConfiguration.getStrategy().name())
                .method(
                        BlockSubscriptionMethodConfigurationResponse.fromDomain(
                                ((BlockSubscriptionConfiguration) subscriptionConfiguration)
                                        .getMethodConfiguration()))
                .initialBlock(blockSubscription.getInitialBlock())
                .confirmationBlocks(blockSubscription.getConfirmationBlocks().value())
                .missingTxRetryBlocks(blockSubscription.getMissingTxRetryBlocks().value())
                .eventInvalidationBlockThreshold(
                        blockSubscription.getEventInvalidationBlockThreshold().value())
                .replayBlockOffset(blockSubscription.getReplayBlockOffset().value())
                .syncBlockLimit(blockSubscription.getSyncBlockLimit().value())
                .build();
    }
}
