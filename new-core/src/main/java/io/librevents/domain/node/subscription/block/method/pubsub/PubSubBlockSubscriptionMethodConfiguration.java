package io.librevents.domain.node.subscription.block.method.pubsub;

import io.librevents.domain.node.subscription.block.method.BlockSubscriptionMethod;
import io.librevents.domain.node.subscription.block.method.BlockSubscriptionMethodConfiguration;

public final class PubSubBlockSubscriptionMethodConfiguration
        extends BlockSubscriptionMethodConfiguration {

    public PubSubBlockSubscriptionMethodConfiguration() {
        super(BlockSubscriptionMethod.PUBSUB);
    }
}
