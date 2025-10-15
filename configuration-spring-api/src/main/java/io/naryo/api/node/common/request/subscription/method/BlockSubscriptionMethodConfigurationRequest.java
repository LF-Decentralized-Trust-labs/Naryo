package io.naryo.api.node.common.request.subscription.method;

import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethodConfiguration;
import io.naryo.domain.node.subscription.block.method.poll.Interval;
import io.naryo.domain.node.subscription.block.method.poll.PollBlockSubscriptionMethodConfiguration;
import io.naryo.domain.node.subscription.block.method.pubsub.PubSubBlockSubscriptionMethodConfiguration;
import jakarta.validation.constraints.NotNull;

public abstract class BlockSubscriptionMethodConfigurationRequest {

    private final @NotNull BlockSubscriptionMethod method;

    protected BlockSubscriptionMethodConfigurationRequest(BlockSubscriptionMethod method) {
        this.method = method;
    }

    public BlockSubscriptionMethodConfiguration toDomain() {
        return switch (method) {
            case PUBSUB -> new PubSubBlockSubscriptionMethodConfiguration();
            case POLL ->
                    new PollBlockSubscriptionMethodConfiguration(
                            new Interval(
                                    ((PollBlockSubscriptionConfigurationMethodRequest) this)
                                            .getInterval()));
        };
    }
}
