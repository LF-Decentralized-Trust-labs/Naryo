package io.naryo.domain.configuration.eventstore;

import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class EventStoreConfiguration {

    private final UUID nodeId;
    private final EventStoreState state;

    protected EventStoreConfiguration(UUID nodeId, EventStoreState state) {
        this.nodeId = nodeId;
        this.state = state;
    }
}
