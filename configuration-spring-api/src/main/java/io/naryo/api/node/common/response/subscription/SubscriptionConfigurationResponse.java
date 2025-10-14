package io.naryo.api.node.common.response.subscription;

import io.naryo.api.node.common.response.subscription.method.BlockSubscriptionMethodConfigurationResponse;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;

public abstract class SubscriptionConfigurationResponse {

    private final String strategy;

    public SubscriptionConfigurationResponse(String strategy) {
        this.strategy = strategy;
    }

    public static SubscriptionConfigurationResponse fromDomain(
            SubscriptionConfiguration subscriptionConfiguration) {
        var blockSubscription = (BlockSubscriptionConfiguration) subscriptionConfiguration;
        return new BlockSubscriptionConfigurationResponse(
                subscriptionConfiguration.getStrategy().name(),
                BlockSubscriptionMethodConfigurationResponse.fromDomain(
                        ((BlockSubscriptionConfiguration) subscriptionConfiguration)
                                .getMethodConfiguration()),
                blockSubscription.getInitialBlock(),
                blockSubscription.getConfirmationBlocks().value(),
                blockSubscription.getMissingTxRetryBlocks().value(),
                blockSubscription.getEventInvalidationBlockThreshold().value(),
                blockSubscription.getReplayBlockOffset().value(),
                blockSubscription.getSyncBlockLimit().value());
    }
}
