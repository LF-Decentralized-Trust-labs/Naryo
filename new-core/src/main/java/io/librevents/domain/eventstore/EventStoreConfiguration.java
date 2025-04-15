package io.librevents.domain.eventstore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class EventStoreConfiguration {

    private final EventStoreType eventStoreType;

    protected EventStoreConfiguration(EventStoreType eventStoreType) {
        this.eventStoreType = eventStoreType;
    }
}
