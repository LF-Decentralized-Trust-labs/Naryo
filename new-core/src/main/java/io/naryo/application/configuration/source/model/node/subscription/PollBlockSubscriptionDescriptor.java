package io.naryo.application.configuration.source.model.node.subscription;

import java.time.Duration;

import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;

public interface PollBlockSubscriptionDescriptor extends BlockSubscriptionDescriptor {

    Duration getInterval();

    void setInterval(Duration interval);

    @Override
    default BlockSubscriptionMethod method() {
        return BlockSubscriptionMethod.POLL;
    }

    @Override
    default SubscriptionDescriptor merge(SubscriptionDescriptor descriptor) {
        var subscription = BlockSubscriptionDescriptor.super.merge(descriptor);

        if (subscription instanceof PollBlockSubscriptionDescriptor other) {
            if (!this.method().equals(other.method())) {
                return subscription;
            }
        }

        return subscription;
    }
}
