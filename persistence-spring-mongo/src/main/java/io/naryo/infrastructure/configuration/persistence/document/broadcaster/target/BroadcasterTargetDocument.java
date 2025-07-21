package io.naryo.infrastructure.configuration.persistence.document.broadcaster.target;

import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public abstract class BroadcasterTargetDocument implements BroadcasterTargetDescriptor {
    @NotNull
    @Getter
    private BroadcasterTargetType type;

    @Setter
    @Getter
    @NotNull
    @NotBlank
    private String destination;

    public BroadcasterTargetDocument(BroadcasterTargetType type, String destination) {
        this.type = type;
        this.destination = destination;
    }

}
