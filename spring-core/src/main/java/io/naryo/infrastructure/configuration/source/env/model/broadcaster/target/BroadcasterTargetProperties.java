package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public abstract class BroadcasterTargetProperties {

    private @NotNull @NotBlank String destination;

    public BroadcasterTargetProperties(String destination) {
        this.destination = destination;
    }
}
