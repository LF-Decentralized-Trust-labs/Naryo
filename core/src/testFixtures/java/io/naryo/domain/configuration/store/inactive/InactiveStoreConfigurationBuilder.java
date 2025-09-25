package io.naryo.domain.configuration.store.inactive;

import io.naryo.domain.configuration.store.StoreConfigurationBuilder;

public final class InactiveStoreConfigurationBuilder
        extends StoreConfigurationBuilder<
                InactiveStoreConfigurationBuilder, InactiveStoreConfiguration> {

    @Override
    public InactiveStoreConfigurationBuilder self() {
        return this;
    }

    @Override
    public InactiveStoreConfiguration build() {
        return new InactiveStoreConfiguration(getNodeId());
    }
}
