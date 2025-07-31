package io.naryo.application.configuration.source.model.event;

import io.naryo.domain.configuration.eventstore.EventStoreType;
import io.naryo.domain.configuration.eventstore.database.DatabaseEngine;

public interface DatabaseBlockEventStoreConfigurationDescriptor
        extends BlockEventStoreConfigurationDescriptor {

    DatabaseEngine getEngine();

    @Override
    default EventStoreType getType() {
        return EventStoreType.DATABASE;
    }
}
