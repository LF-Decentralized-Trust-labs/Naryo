package io.naryo.api.filter.request;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.event.EventFilterSpecification;
import io.naryo.domain.filter.event.EventFilterVisibilityConfiguration;
import io.naryo.domain.filter.event.FilterSyncState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GlobalEventFilterRequest(
        @Schema(example = "global-event") String type,
        @NotNull UUID id,
        @NotBlank String name,
        @NotNull UUID nodeId,
        @NotNull EventFilterSpecification specification,
        Set<ContractEventStatus> statuses,
        @NotNull FilterSyncState filterSyncState,
        EventFilterVisibilityConfiguration visibilityConfiguration)
        implements FilterRequest {

    public GlobalEventFilterRequest {
        type = "global-event";
        if (statuses == null || statuses.isEmpty()) {
            statuses = Set.of(ContractEventStatus.values());
        }
        if (visibilityConfiguration == null) {
            visibilityConfiguration = EventFilterVisibilityConfiguration.visible();
        }
    }
}
