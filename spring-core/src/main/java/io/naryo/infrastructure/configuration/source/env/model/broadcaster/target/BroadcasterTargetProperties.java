package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Setter
@Getter
@NoArgsConstructor
public abstract class BroadcasterTargetProperties implements BroadcasterTargetDescriptor {

    private @Nullable @NotBlank String destination;

    public BroadcasterTargetProperties(String destination) {
        this.destination = destination;
    }

    public Optional<String> getDestination() {
        return Optional.ofNullable(destination);
    }
}
