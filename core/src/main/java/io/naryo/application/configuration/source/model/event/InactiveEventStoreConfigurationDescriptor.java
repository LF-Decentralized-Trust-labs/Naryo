package io.naryo.application.configuration.source.model.event;

import io.naryo.domain.configuration.store.StoreState;

public interface InactiveEventStoreConfigurationDescriptor
        extends EventStoreConfigurationDescriptor {

    @Override
    default StoreState getState() {
        return StoreState.INACTIVE;
    }
}
