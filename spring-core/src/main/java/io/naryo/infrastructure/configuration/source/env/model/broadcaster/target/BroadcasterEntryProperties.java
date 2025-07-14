package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import java.util.UUID;

import io.naryo.application.configuration.source.model.broadcaster.BroadcasterDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public final class BroadcasterEntryProperties implements BroadcasterDescriptor {

    private final @NotNull UUID id;
    private @NotNull UUID configurationId;
    private @Valid @NotNull BroadcasterTargetDescriptor target;

    public BroadcasterEntryProperties(
            UUID id, UUID configurationId, BroadcasterTargetDescriptor target) {
        this.id = id;
        this.configurationId = configurationId;
        this.target = target;
    }

    @Override
    public UUID id() {
        return id;
    }

    @Override
    public UUID configurationId() {
        return configurationId;
    }

    @Override
    public BroadcasterTargetDescriptor target() {
        return target;
    }

    @Override
    public void setConfigurationId(UUID configurationId) {
        this.configurationId = configurationId;
    }

    @Override
    public void setTarget(BroadcasterTargetDescriptor target) {
        this.target = target;
    }
}
