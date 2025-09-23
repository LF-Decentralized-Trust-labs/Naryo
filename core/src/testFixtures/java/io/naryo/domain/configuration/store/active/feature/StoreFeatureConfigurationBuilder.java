package io.naryo.domain.configuration.store.active.feature;

import io.naryo.domain.DomainBuilder;

public abstract class StoreFeatureConfigurationBuilder<
                T extends StoreFeatureConfigurationBuilder<T, Y>,
                Y extends StoreFeatureConfiguration>
        implements DomainBuilder<T, Y> {}
