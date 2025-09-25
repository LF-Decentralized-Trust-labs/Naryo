package io.naryo.domain.node.subscription;

import io.naryo.domain.DomainBuilder;

public abstract class SubscriptionConfigurationBuilder<
                T extends SubscriptionConfigurationBuilder<T, Y>,
                Y extends SubscriptionConfiguration>
        implements DomainBuilder<T, Y> {}
