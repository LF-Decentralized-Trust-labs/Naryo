package io.naryo.api.filter.request;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.EventFilterSpecification;
import io.naryo.domain.filter.event.EventFilterVisibilityConfiguration;
import io.naryo.domain.filter.event.FilterSyncState;
import io.naryo.domain.filter.event.GlobalEventFilter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record UpdateGlobalEventFilterRequest(
    @Schema(example = "global-event") String type,
    @NotBlank String name,
    @NotNull UUID nodeId,
    @NotNull EventFilterSpecification specification,
    Set<ContractEventStatus> statuses,
    @NotNull FilterSyncState filterSyncState,
    EventFilterVisibilityConfiguration visibilityConfiguration
) implements UpdateFilterRequest {

    public UpdateGlobalEventFilterRequest {
        type = "global-event";
    }

    public static GlobalEventFilter toDomain(UpdateGlobalEventFilterRequest req, UUID idFromPath) {
        return new GlobalEventFilter(
            idFromPath,
            new FilterName(req.name()),
            req.nodeId(),
            req.specification(),
            req.statuses(),
            req.filterSyncState(),
            req.visibilityConfiguration()
        );
    }
}
