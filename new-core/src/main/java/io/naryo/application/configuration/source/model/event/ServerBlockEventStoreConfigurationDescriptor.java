package io.naryo.application.configuration.source.model.event;

import java.util.Optional;

import io.naryo.domain.configuration.eventstore.EventStoreType;
import io.naryo.domain.configuration.eventstore.server.ServerType;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface ServerBlockEventStoreConfigurationDescriptor
        extends BlockEventStoreConfigurationDescriptor {

    Optional<ServerType> getServerType();

    void setServerType(ServerType serverType);

    @Override
    default Optional<EventStoreType> getType() {
        return Optional.of(EventStoreType.SERVER);
    }

    @Override
    default EventStoreConfigurationDescriptor merge(EventStoreConfigurationDescriptor other) {
        BlockEventStoreConfigurationDescriptor.super.merge(other);

        if (other instanceof ServerBlockEventStoreConfigurationDescriptor otherServer) {
            mergeOptionals(this::setServerType, this.getServerType(), otherServer.getServerType());
        }

        return this;
    }
}
