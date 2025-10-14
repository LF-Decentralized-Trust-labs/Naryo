package io.naryo.api.node.common.response.subscription.method;

import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethodConfiguration;
import io.naryo.domain.node.subscription.block.method.poll.PollBlockSubscriptionMethodConfiguration;
import io.naryo.domain.node.subscription.block.method.pubsub.PubSubBlockSubscriptionMethodConfiguration;

public abstract class BlockSubscriptionMethodConfigurationResponse {

    private final String method;

    public BlockSubscriptionMethodConfigurationResponse(String method) {
        this.method = method;
    }

    public static BlockSubscriptionMethodConfigurationResponse fromDomain(
            BlockSubscriptionMethodConfiguration methodConfiguration) {
        return switch (methodConfiguration) {
            case PubSubBlockSubscriptionMethodConfiguration pubsub ->
                    new PubSubBlockSubscriptionConfigurationMethodResponse(
                            pubsub.getMethod().name());
            case PollBlockSubscriptionMethodConfiguration poll ->
                    new PollBlockSubscriptionConfigurationMethodResponse(
                            poll.getMethod().name(), poll.getInterval().value());
            default -> throw new IllegalStateException("Unexpected value: " + methodConfiguration);
        };
    }
}
