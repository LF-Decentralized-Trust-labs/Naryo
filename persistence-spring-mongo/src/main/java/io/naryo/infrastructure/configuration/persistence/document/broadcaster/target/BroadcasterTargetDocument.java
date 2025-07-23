package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import java.util.Optional;

import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public abstract class BroadcasterTargetDocument implements BroadcasterTargetDescriptor {
    @Nullable @NotBlank private String destination;

    public BroadcasterTargetDocument(String destination) {
        this.destination = destination;
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
