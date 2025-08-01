package io.naryo.domain.configuration.eventstore.active;

import java.util.UUID;

import io.naryo.domain.configuration.Configuration;
import io.naryo.domain.configuration.eventstore.EventStoreConfiguration;
import io.naryo.domain.configuration.eventstore.EventStoreState;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public abstract class ActiveEventStoreConfiguration extends EventStoreConfiguration
        implements Configuration {

    private final EventStoreType type;
    private final EventStoreStrategy strategy;

    protected ActiveEventStoreConfiguration(
            UUID nodeId, EventStoreType type, EventStoreStrategy strategy) {
        super(nodeId, EventStoreState.ACTIVE);
        this.type = type;
        this.strategy = strategy;
    }
}
