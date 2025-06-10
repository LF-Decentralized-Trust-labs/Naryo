package io.naryo.domain.filter.event;

import java.util.List;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterName;

public class GlobalEventFilter extends EventFilter {

    public GlobalEventFilter(
            UUID id,
            FilterName name,
            UUID nodeId,
            EventFilterSpecification specification,
            List<ContractEventStatus> statuses,
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
            List<ContractEventStatus> statuses,
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
