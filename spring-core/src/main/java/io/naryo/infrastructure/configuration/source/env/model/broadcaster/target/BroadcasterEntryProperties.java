package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.broadcaster.BroadcasterDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public final class BroadcasterEntryProperties implements BroadcasterDescriptor {

    private final @NotNull UUID id;
    private @Nullable UUID configurationId;
    private @Valid @Nullable BroadcasterTargetDescriptor target;

    public BroadcasterEntryProperties(
            UUID id, UUID configurationId, BroadcasterTargetDescriptor target) {
        this.id = id;
        this.configurationId = configurationId;
        this.target = target;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Optional<UUID> getConfigurationId() {
        return Optional.ofNullable(configurationId);
    }

    @Override
    public Optional<BroadcasterTargetDescriptor> getTarget() {
        return Optional.ofNullable(target);
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
