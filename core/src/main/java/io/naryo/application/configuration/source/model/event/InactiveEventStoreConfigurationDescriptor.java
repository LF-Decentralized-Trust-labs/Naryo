package io.naryo.application.configuration.source.model.event;

import io.naryo.domain.configuration.eventstore.EventStoreState;

public interface InactiveEventStoreConfigurationDescriptor
        extends EventStoreConfigurationDescriptor {

    @Override
    default EventStoreState getState() {
        return EventStoreState.INACTIVE;
    }
}
