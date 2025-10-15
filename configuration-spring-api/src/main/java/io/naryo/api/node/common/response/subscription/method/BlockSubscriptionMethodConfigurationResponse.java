package io.naryo.api.node.common.response.subscription.method;

import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethodConfiguration;
import io.naryo.domain.node.subscription.block.method.poll.PollBlockSubscriptionMethodConfiguration;
import io.naryo.domain.node.subscription.block.method.pubsub.PubSubBlockSubscriptionMethodConfiguration;

public sealed interface BlockSubscriptionMethodConfigurationResponse
        permits PollBlockSubscriptionConfigurationMethodResponse,
                PubSubBlockSubscriptionConfigurationMethodResponse {

    static BlockSubscriptionMethodConfigurationResponse fromDomain(
            BlockSubscriptionMethodConfiguration methodConfiguration) {
        return switch (methodConfiguration) {
            case PubSubBlockSubscriptionMethodConfiguration pubsub ->
                    PubSubBlockSubscriptionConfigurationMethodResponse.builder()
                            .method(pubsub.getMethod().name())
                            .build();
            case PollBlockSubscriptionMethodConfiguration poll ->
                    PollBlockSubscriptionConfigurationMethodResponse.builder()
                            .method(poll.getMethod().name())
                            .interval(poll.getInterval().value())
                            .build();
            default -> throw new IllegalStateException("Unexpected value: " + methodConfiguration);
        };
    }
}
