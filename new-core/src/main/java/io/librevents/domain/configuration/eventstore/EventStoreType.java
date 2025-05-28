package io.librevents.domain.configuration.eventstore;

import io.librevents.domain.configuration.ConfigurationType;

public enum EventStoreType implements ConfigurationType {
    DATABASE,
    SERVER;

    @Override
    public String getName() {
        return this.name();
    }
}
