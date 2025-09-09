package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import java.util.List;
import java.util.UUID;

import io.naryo.application.configuration.source.model.broadcaster.target.FilterBroadcasterTargetDescriptor;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class FilterBroadcasterTargetProperties extends BroadcasterTargetProperties
        implements FilterBroadcasterTargetDescriptor {

    private @NotNull UUID filterId;

    public FilterBroadcasterTargetProperties(List<String> destinations, UUID filterId) {
        super(destinations);
        this.filterId = filterId;
    }
}
