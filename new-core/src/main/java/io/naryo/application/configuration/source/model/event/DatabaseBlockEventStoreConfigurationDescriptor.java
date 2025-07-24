package io.naryo.application.configuration.source.model.event;

import java.util.Optional;

import io.naryo.domain.configuration.eventstore.EventStoreType;
import io.naryo.domain.configuration.eventstore.database.DatabaseEngine;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface DatabaseBlockEventStoreConfigurationDescriptor
        extends BlockEventStoreConfigurationDescriptor {

    Optional<DatabaseEngine> getEngine();

    void setEngine(DatabaseEngine engine);

    @Override
    default Optional<EventStoreType> getType() {
        return Optional.of(EventStoreType.DATABASE);
    }

    @Override
    default EventStoreConfigurationDescriptor merge(EventStoreConfigurationDescriptor other) {
        BlockEventStoreConfigurationDescriptor.super.merge(other);

        if (other instanceof DatabaseBlockEventStoreConfigurationDescriptor otherDatabase) {
            mergeOptionals(this::setEngine, this.getEngine(), otherDatabase.getEngine());
        }

        return this;
    }
}
