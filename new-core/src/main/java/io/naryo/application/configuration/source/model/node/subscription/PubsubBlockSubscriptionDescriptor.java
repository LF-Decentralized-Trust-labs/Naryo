package io.naryo.application.configuration.source.model.node.subscription;

import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;

public interface PubsubBlockSubscriptionDescriptor extends BlockSubscriptionDescriptor {

    @Override
    default BlockSubscriptionMethod method() {
        return BlockSubscriptionMethod.PUBSUB;
    }

    @Override
    default SubscriptionDescriptor merge(SubscriptionDescriptor descriptor) {
        var subscription = BlockSubscriptionDescriptor.super.merge(descriptor);

        if (subscription instanceof PubsubBlockSubscriptionDescriptor other) {
            if (!this.method().equals(other.method())) {
                return subscription;
            }
        }

        return subscription;
    }
}
