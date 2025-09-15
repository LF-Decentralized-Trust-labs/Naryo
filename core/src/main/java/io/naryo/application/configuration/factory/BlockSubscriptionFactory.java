package io.naryo.application.configuration.factory;

import io.naryo.application.configuration.source.model.node.subscription.BlockSubscriptionDescriptor;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;

public interface BlockSubscriptionFactory {
    BlockSubscriptionConfiguration create(BlockSubscriptionDescriptor descriptor);
}
