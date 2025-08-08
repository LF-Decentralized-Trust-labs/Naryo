package io.naryo.infrastructure.configuration.source.env.model.store;

import java.util.UUID;

import io.naryo.application.configuration.source.model.store.StoreConfigurationDescriptor;
import jakarta.validation.constraints.NotNull;

public abstract class StoreConfigurationProperties implements StoreConfigurationDescriptor {

    private final @NotNull UUID nodeId;

    protected StoreConfigurationProperties(UUID nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public UUID getNodeId() {
        return nodeId;
    }
}
