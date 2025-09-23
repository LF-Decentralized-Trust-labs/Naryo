package io.naryo.domain.node.subscription.method;

import io.naryo.domain.DomainBuilder;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethodConfiguration;

public abstract class BlockSubscriptionMethodConfigurationBuilder<
                T extends BlockSubscriptionMethodConfigurationBuilder<T, Y>,
                Y extends BlockSubscriptionMethodConfiguration>
        implements DomainBuilder<T, Y> {}
