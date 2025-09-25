package io.naryo.domain.node.interaction;

import io.naryo.domain.DomainBuilder;

public abstract class InteractionConfigurationBuilder<
                T extends InteractionConfigurationBuilder<T, Y>, Y extends InteractionConfiguration>
        implements DomainBuilder<T, Y> {}
