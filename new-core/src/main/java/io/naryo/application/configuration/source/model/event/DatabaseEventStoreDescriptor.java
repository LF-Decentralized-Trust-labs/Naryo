package io.naryo.application.configuration.source.model.event;

import io.naryo.domain.configuration.eventstore.EventStoreType;

public interface DatabaseEventStoreDescriptor extends EventStoreDescriptor {

    @Override
    default EventStoreType getType() {
        return EventStoreType.DATABASE;
    }
}
