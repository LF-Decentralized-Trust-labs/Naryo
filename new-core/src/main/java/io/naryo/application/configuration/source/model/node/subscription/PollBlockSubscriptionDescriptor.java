package io.naryo.application.configuration.source.model.node.subscription;

import java.time.Duration;
import java.util.Optional;

import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface PollBlockSubscriptionDescriptor extends BlockSubscriptionDescriptor {

    Optional<Duration> getInterval();

    void setInterval(Duration interval);

    @Override
    default BlockSubscriptionMethod getMethod() {
        return BlockSubscriptionMethod.POLL;
    }

    @Override
    default SubscriptionDescriptor merge(SubscriptionDescriptor other) {
        if (!(other instanceof PollBlockSubscriptionDescriptor otherPollBlockSubscription)) {
            return this;
        }

        mergeOptionals(
                this::setInterval, this.getInterval(), otherPollBlockSubscription.getInterval());

        return BlockSubscriptionDescriptor.super.merge(other);
    }
}
