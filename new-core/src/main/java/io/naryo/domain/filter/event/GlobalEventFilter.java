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
            SyncState syncState,
            EventFilterVisibilityConfiguration visibilityConfiguration) {
        super(
                id,
                name,
                nodeId,
                EventFilterScope.GLOBAL,
                specification,
                statuses,
                syncState,
                visibilityConfiguration);
    }

    public GlobalEventFilter(
            UUID id,
            FilterName name,
            UUID nodeId,
            EventFilterSpecification specification,
            Set<ContractEventStatus> statuses,
            SyncState syncState) {
        super(
                id,
                name,
                nodeId,
                EventFilterScope.GLOBAL,
                specification,
                statuses,
                syncState,
                EventFilterVisibilityConfiguration.visible());
    }
}
