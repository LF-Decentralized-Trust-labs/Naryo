package io.naryo.domain.configuration.eventstore;

import io.naryo.domain.configuration.ConfigurationType;

public enum EventStoreType implements ConfigurationType {
    DATABASE,
    SERVER;

    @Override
    public String getName() {
        return this.name();
    }
}
