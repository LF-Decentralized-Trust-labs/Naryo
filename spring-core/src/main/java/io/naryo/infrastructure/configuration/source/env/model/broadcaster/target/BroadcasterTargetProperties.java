package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import java.util.Set;

import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public abstract class BroadcasterTargetProperties implements BroadcasterTargetDescriptor {

    private @Nullable @NotEmpty Set<String> destinations;

    public BroadcasterTargetProperties(@Nullable Set<String> destinations) {
        this.destinations = destinations;
    }

    @Nullable
    public Set<String> getDestinations() {
        return destinations;
    }
}
