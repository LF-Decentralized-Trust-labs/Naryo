package io.naryo.application.configuration.source.model.node.subscription;

import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;

public interface PubsubBlockSubscriptionDescriptor extends BlockSubscriptionDescriptor {

    @Override
    default BlockSubscriptionMethod method() {
        return BlockSubscriptionMethod.PUBSUB;
    }

    @Override
    default SubscriptionDescriptor merge(SubscriptionDescriptor descriptor) {
        BlockSubscriptionDescriptor.super.merge(descriptor);

        if (descriptor instanceof PubsubBlockSubscriptionDescriptor other) {
            if (!this.method().equals(other.method())) {
                return descriptor;
            }
        }

        return this;
    }
}
