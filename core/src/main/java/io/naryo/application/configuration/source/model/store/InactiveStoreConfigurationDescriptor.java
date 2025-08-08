package io.naryo.application.configuration.source.model.store;

import io.naryo.domain.configuration.store.StoreState;

public interface InactiveStoreConfigurationDescriptor extends StoreConfigurationDescriptor {

    @Override
    default StoreState getState() {
        return StoreState.INACTIVE;
    }
}
