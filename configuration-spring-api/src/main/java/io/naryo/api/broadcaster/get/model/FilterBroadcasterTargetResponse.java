package io.naryo.api.broadcaster.get.model;

import java.util.List;
import java.util.UUID;

import io.naryo.domain.broadcaster.target.FilterEventBroadcasterTarget;
import io.naryo.domain.common.Destination;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(description = "Filter event broadcaster target")
@Getter
public final class FilterBroadcasterTargetResponse extends BroadcasterTargetResponse {

    private final UUID filterId;

    private FilterBroadcasterTargetResponse(
            @NotEmpty List<Destination> destinations, @NotNull UUID filterId) {
        super(destinations);
        this.filterId = filterId;
    }

    public static FilterBroadcasterTargetResponse fromDomain(
            FilterEventBroadcasterTarget filterEventBroadcasterTarget) {
        return new FilterBroadcasterTargetResponse(
                filterEventBroadcasterTarget.getDestinations(),
                filterEventBroadcasterTarget.getFilterId());
    }
}
