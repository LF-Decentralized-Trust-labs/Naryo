package io.naryo.api.filter.request;


import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.ContractEventFilter;
import io.naryo.domain.filter.event.EventFilterSpecification;
import io.naryo.domain.filter.event.EventFilterVisibilityConfiguration;
import io.naryo.domain.filter.event.FilterSyncState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record UpdateContractEventFilterRequest(
    @Schema(example = "contract-event") String type,
    @NotBlank String name,
    @NotNull UUID nodeId,
    @NotNull EventFilterSpecification specification,
    Set<ContractEventStatus> statuses,
    @NotNull FilterSyncState filterSyncState,
    EventFilterVisibilityConfiguration visibilityConfiguration,
    @NotBlank String contractAddress
) implements UpdateFilterRequest {

    public UpdateContractEventFilterRequest {
        type = "contract-event";
    }

    public static ContractEventFilter toDomain(UpdateContractEventFilterRequest req, UUID idFromPath) {
        return new ContractEventFilter(
            idFromPath,
            new FilterName(req.name()),
            req.nodeId(),
            req.specification(),
            req.statuses(),
            req.filterSyncState(),
            req.visibilityConfiguration(),
            req.contractAddress()
        );
    }
}
