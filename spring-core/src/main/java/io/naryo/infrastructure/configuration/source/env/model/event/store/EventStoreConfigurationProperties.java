package io.naryo.infrastructure.configuration.source.env.model.event.store;

import java.util.UUID;

import io.naryo.application.configuration.source.model.event.EventStoreConfigurationDescriptor;
import jakarta.validation.constraints.NotNull;

public abstract class EventStoreConfigurationProperties
        implements EventStoreConfigurationDescriptor {

    private final @NotNull UUID nodeId;

    protected EventStoreConfigurationProperties(UUID nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public UUID getNodeId() {
        return nodeId;
    }
}
