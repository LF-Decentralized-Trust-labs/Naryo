package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import java.util.Optional;

import io.naryo.domain.broadcaster.BroadcasterTargetType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public abstract class BroadcasterTargetProperties {

    private final @NotNull BroadcasterTargetType type;

    @Nullable @NotBlank private String destination;

    public BroadcasterTargetProperties(BroadcasterTargetType type, String destination) {
        this.type = type;
        this.destination = destination;
    }

    public BroadcasterTargetType getType() {
        return this.type;
    }

    public Optional<String> getDestination() {
        return Optional.ofNullable(destination);
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
