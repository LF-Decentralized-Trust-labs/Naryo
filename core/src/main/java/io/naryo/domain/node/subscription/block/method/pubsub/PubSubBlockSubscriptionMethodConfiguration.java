package io.naryo.domain.node.subscription.block.method.pubsub;

import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethodConfiguration;

public final class PubSubBlockSubscriptionMethodConfiguration
        extends BlockSubscriptionMethodConfiguration {

    public PubSubBlockSubscriptionMethodConfiguration() {
        super(BlockSubscriptionMethod.PUBSUB);
    }
}
