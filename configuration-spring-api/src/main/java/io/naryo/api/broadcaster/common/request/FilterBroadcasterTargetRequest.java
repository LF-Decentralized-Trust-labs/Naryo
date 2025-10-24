package io.naryo.api.broadcaster.common.request;

import java.util.List;
import java.util.UUID;

import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.target.FilterEventBroadcasterTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(description = "Filter event broadcaster target request")
@Getter
public final class FilterBroadcasterTargetRequest extends BroadcasterTargetRequest {

    private final @NotNull UUID filterId;

    public FilterBroadcasterTargetRequest(
            @NotEmpty List<String> destinations, @NotNull UUID filterId) {
        super(destinations);
        this.filterId = filterId;
    }

    @Override
    public BroadcasterTarget toDomain() {
        return new FilterEventBroadcasterTarget(getDomainDestinations(), filterId);
    }
}
