package io.naryo.domain.configuration.eventstore;

import java.util.UUID;

import io.naryo.domain.configuration.Configuration;
import io.naryo.domain.configuration.ConfigurationType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class EventStoreConfiguration implements Configuration {

    private final UUID nodeId;
    private final EventStoreType type;
    private final EventStoreStrategy strategy;

    protected EventStoreConfiguration(
            UUID nodeId, EventStoreType type, EventStoreStrategy strategy) {
        this.nodeId = nodeId;
        this.type = type;
        this.strategy = strategy;
    }

    @Override
    public ConfigurationType getType() {
        return type;
    }
}
