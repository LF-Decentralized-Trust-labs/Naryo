package io.naryo.api.broadcaster.common.request;

import java.util.List;
import java.util.UUID;

import io.naryo.domain.broadcaster.BroadcasterTargetType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public final class FilterBroadcasterTargetDTO extends BroadcasterTargetDTO {

    private final @NotNull UUID filterId;

    FilterBroadcasterTargetDTO(@NotEmpty List<String> destinations, @NotNull UUID filterId) {
        super(BroadcasterTargetType.FILTER, destinations);
        this.filterId = filterId;
    }
}
