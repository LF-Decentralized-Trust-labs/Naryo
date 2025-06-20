package io.naryo.domain.configuration.eventstore;

import io.naryo.domain.configuration.Configuration;
import io.naryo.domain.configuration.ConfigurationType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public abstract class EventStoreConfiguration implements Configuration {

    private final EventStoreType type;

    protected EventStoreConfiguration(EventStoreType type) {
        this.type = type;
    }

    @Override
    public ConfigurationType getType() {
        return type;
    }

    public abstract EventStoreConfiguration merge(EventStoreConfiguration other);
}
