package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import io.naryo.domain.broadcaster.BroadcasterTargetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public abstract class BroadcasterTargetProperties {

    private final @Getter @NotNull BroadcasterTargetType type;
    private @Setter @Getter @NotNull @NotBlank String destination;

    public BroadcasterTargetProperties(BroadcasterTargetType type, String destination) {
        this.type = type;
        this.destination = destination;
    }
}
