package io.naryo.api.filter.request;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateGlobalEventFilterRequest(
        @Schema(example = "global-event") String type,
        @NotBlank String name,
        @NotNull UUID nodeId,
        @NotNull EventFilterSpecification specification,
        Set<ContractEventStatus> statuses,
        @NotNull FilterSyncState filterSyncState,
        EventFilterVisibilityConfiguration visibilityConfiguration)
        implements CreateFilterRequest {

    public CreateGlobalEventFilterRequest {
        type = "global-event";
    }

    public static EventFilter toDomain(CreateGlobalEventFilterRequest req) {
        var name = new FilterName(req.name());
        return new GlobalEventFilter(
                UUID.randomUUID(),
                name,
                req.nodeId(),
                req.specification(),
                req.statuses(),
                req.filterSyncState(),
                req.visibilityConfiguration());
    }
}
