package io.naryo.domain.configuration.eventstore;

import io.naryo.domain.configuration.Configuration;
import io.naryo.domain.configuration.ConfigurationType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public abstract class EventStoreConfiguration implements Configuration {

    private final EventStoreType type;
    private final EventStoreStrategy strategy;

    protected EventStoreConfiguration(EventStoreType type, EventStoreStrategy strategy) {
        this.type = type;
        this.strategy = strategy;
    }

    @Override
    public ConfigurationType getType() {
        return type;
    }
}
