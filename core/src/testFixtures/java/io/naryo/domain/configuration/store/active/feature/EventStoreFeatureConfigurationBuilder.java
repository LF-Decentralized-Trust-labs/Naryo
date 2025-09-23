package io.naryo.domain.configuration.store.active.feature;

import io.naryo.domain.configuration.store.active.feature.event.EventStoreConfiguration;

public abstract class EventStoreFeatureConfigurationBuilder<
                T extends EventStoreFeatureConfigurationBuilder<T, Y>,
                Y extends EventStoreConfiguration>
        extends StoreFeatureConfigurationBuilder<T, Y> {}
