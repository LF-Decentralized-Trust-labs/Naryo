package io.librevents.domain.eventstore;

import io.librevents.domain.common.configuration.Configuration;
import io.librevents.domain.common.configuration.ConfigurationType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public abstract class EventStoreConfiguration implements Configuration {

    private final EventStoreType eventStoreType;

    protected EventStoreConfiguration(EventStoreType eventStoreType) {
        this.eventStoreType = eventStoreType;
    }

    @Override
    public ConfigurationType getType() {
        return eventStoreType;
    }
}
