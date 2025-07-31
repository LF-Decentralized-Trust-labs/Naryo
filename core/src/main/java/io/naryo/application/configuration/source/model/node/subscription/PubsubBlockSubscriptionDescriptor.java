package io.naryo.application.configuration.source.model.node.subscription;

import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;

public interface PubsubBlockSubscriptionDescriptor extends BlockSubscriptionDescriptor {

    @Override
    default BlockSubscriptionMethod getMethod() {
        return BlockSubscriptionMethod.PUBSUB;
    }

    @Override
    default SubscriptionDescriptor merge(SubscriptionDescriptor other) {
        if (!(other instanceof PubsubBlockSubscriptionDescriptor otherPubsubSubscription)) {
            return this;
        }

        return BlockSubscriptionDescriptor.super.merge(otherPubsubSubscription);
    }
}
