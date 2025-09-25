package io.naryo.domain.node.subscription.method;

import io.naryo.domain.node.subscription.block.method.pubsub.PubSubBlockSubscriptionMethodConfiguration;

public final class PubSubBlockSubscriptionMethodConfigurationBuilder
        extends BlockSubscriptionMethodConfigurationBuilder<
                PubSubBlockSubscriptionMethodConfigurationBuilder,
                PubSubBlockSubscriptionMethodConfiguration> {
    @Override
    public PubSubBlockSubscriptionMethodConfigurationBuilder self() {
        return this;
    }

    @Override
    public PubSubBlockSubscriptionMethodConfiguration build() {
        return new PubSubBlockSubscriptionMethodConfiguration();
    }
}
