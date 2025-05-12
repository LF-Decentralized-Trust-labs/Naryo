package io.librevents.domain.eventstore;

import io.librevents.domain.common.configuration.ConfigurationType;

public enum EventStoreType implements ConfigurationType {
    DATABASE,
    SERVER;

    @Override
    public String getName() {
        return this.name();
    }
}
