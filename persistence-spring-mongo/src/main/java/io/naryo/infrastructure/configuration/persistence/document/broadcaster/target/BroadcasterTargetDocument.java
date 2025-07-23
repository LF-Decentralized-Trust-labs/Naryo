package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import java.util.Optional;

import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public abstract class BroadcasterTargetDocument implements BroadcasterTargetDescriptor {
    @NotNull private BroadcasterTargetType type;

    @Nullable @NotBlank private String destination;

    public BroadcasterTargetDocument(BroadcasterTargetType type, String destination) {
        this.type = type;
        this.destination = destination;
    }

    @Override
    public BroadcasterTargetType getType() {
        return this.type;
    }

    @Override
    public Optional<String> getDestination() {
        return Optional.ofNullable(destination);
    }

    @Override
    public void setDestination(String destination) {
        this.destination = destination;
    }
}
