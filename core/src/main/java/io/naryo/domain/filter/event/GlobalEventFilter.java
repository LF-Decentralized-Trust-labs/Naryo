package io.naryo.domain.filter.event;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterName;

public class GlobalEventFilter extends EventFilter {

    public GlobalEventFilter(
            UUID id,
            FilterName name,
            UUID nodeId,
            EventFilterSpecification specification,
            Set<ContractEventStatus> statuses,
            FilterSyncState filterSyncState,
            EventFilterVisibilityConfiguration visibilityConfiguration) {
        super(
                id,
                name,
                nodeId,
                EventFilterScope.GLOBAL,
                specification,
                statuses,
                filterSyncState,
                visibilityConfiguration);
    }

    public GlobalEventFilter(
            UUID id,
            FilterName name,
            UUID nodeId,
            EventFilterSpecification specification,
            Set<ContractEventStatus> statuses,
            FilterSyncState filterSyncState) {
        super(
                id,
                name,
                nodeId,
                EventFilterScope.GLOBAL,
                specification,
                statuses,
                filterSyncState,
                EventFilterVisibilityConfiguration.visible());
    }
}
