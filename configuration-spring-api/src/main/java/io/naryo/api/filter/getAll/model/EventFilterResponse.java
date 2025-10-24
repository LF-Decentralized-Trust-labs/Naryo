package io.naryo.api.filter.getAll.model;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.event.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Base class for event filter")
@Getter
public abstract class EventFilterResponse extends FilterResponse {

    private final EventFilterSpecificationResponse specification;
    private final Set<ContractEventStatus> statuses;
    private final FilterSyncStateResponse filterSyncState;
    private final EventFilterVisibilityConfigurationResponse visibilityConfiguration;

    protected EventFilterResponse(
            UUID id,
            String name,
            UUID nodeId,
            String currentItemHash,
            EventFilterSpecificationResponse specification,
            Set<ContractEventStatus> statuses,
            FilterSyncStateResponse filterSyncState,
            EventFilterVisibilityConfigurationResponse visibilityConfiguration) {
        super(id, name, nodeId, currentItemHash);
        this.specification = specification;
        this.statuses = statuses;
        this.filterSyncState = filterSyncState;
        this.visibilityConfiguration = visibilityConfiguration;
    }

    public static EventFilterResponse fromDomain(EventFilter eventFilter, String currentItemHash) {
        return switch (eventFilter) {
            case ContractEventFilter cef ->
                    ContractEventFilterResponse.fromDomain(cef, currentItemHash);
            case GlobalEventFilter gef ->
                    GlobalEventFilterResponse.fromDomain(gef, currentItemHash);
            default -> throw new IllegalStateException("Unexpected value: " + eventFilter);
        };
    }
}
