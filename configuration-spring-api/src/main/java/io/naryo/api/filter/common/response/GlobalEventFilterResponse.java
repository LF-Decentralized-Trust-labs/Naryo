package io.naryo.api.filter.common.response;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.event.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Global event filter")
@Getter
public final class GlobalEventFilterResponse extends EventFilterResponse {

    private GlobalEventFilterResponse(
            UUID id,
            String name,
            UUID nodeId,
            String currentItemHash,
            EventFilterSpecificationResponse specification,
            Set<ContractEventStatus> statuses,
            FilterSyncStateResponse filterSyncState,
            EventFilterVisibilityConfigurationResponse visibilityConfiguration) {
        super(
                id,
            name,
                nodeId,
                currentItemHash,
            specification,
                statuses,
                filterSyncState,
                visibilityConfiguration);
    }

    public static EventFilterResponse fromDomain(
            GlobalEventFilter globalEventFilter, String currentItemHash) {
        return new GlobalEventFilterResponse(
                globalEventFilter.getId(),
            globalEventFilter.getName().value(),
                globalEventFilter.getNodeId(),
                currentItemHash,
            EventFilterSpecificationResponse.fromDomain(globalEventFilter.getSpecification()),
                globalEventFilter.getStatuses(),
                FilterSyncStateResponse.fromDomain(globalEventFilter.getFilterSyncState()),
                EventFilterVisibilityConfigurationResponse.fromDomain(
                        globalEventFilter.getVisibilityConfiguration()));
    }
}
