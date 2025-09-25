package io.naryo.domain.configuration.store.active.feature;

import io.naryo.domain.common.Destination;
import io.naryo.domain.configuration.store.active.feature.filter.FilterStoreConfiguration;
import org.instancio.Instancio;

public final class FilterStoreConfigurationBuilder
        extends StoreFeatureConfigurationBuilder<
                FilterStoreConfigurationBuilder, FilterStoreConfiguration> {

    private Destination destination;

    @Override
    public FilterStoreConfigurationBuilder self() {
        return this;
    }

    @Override
    public FilterStoreConfiguration build() {
        return new FilterStoreConfiguration(getDestination());
    }

    public FilterStoreConfigurationBuilder withDestination(Destination destination) {
        this.destination = destination;
        return self();
    }

    public Destination getDestination() {
        return destination == null ? Instancio.create(Destination.class) : destination;
    }
}
