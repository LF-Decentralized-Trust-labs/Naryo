package io.naryo.application.configuration.source.model.event;

import io.naryo.domain.configuration.eventstore.EventStoreType;
import io.naryo.domain.configuration.eventstore.server.ServerType;

public interface ServerBlockEventStoreConfigurationDescriptor
        extends BlockEventStoreConfigurationDescriptor {

    ServerType getServerType();

    @Override
    default EventStoreType getType() {
        return EventStoreType.SERVER;
    }
}
