package io.naryo.application.configuration.source.model.node.subscription;

import java.time.Duration;
import java.util.Optional;

import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface PollBlockSubscriptionDescriptor extends BlockSubscriptionDescriptor {

    Optional<Duration> getInterval();

    void setInterval(Duration interval);

    @Override
    default BlockSubscriptionMethod method() {
        return BlockSubscriptionMethod.POLL;
    }

    @Override
    default SubscriptionDescriptor merge(SubscriptionDescriptor descriptor) {
        BlockSubscriptionDescriptor.super.merge(descriptor);

        if (descriptor instanceof PollBlockSubscriptionDescriptor other) {
            if (!this.method().equals(other.method())) {
                return descriptor;
            }

            mergeOptionals(this::setInterval, this.getInterval(), other.getInterval());
        }

        return this;
    }
}
