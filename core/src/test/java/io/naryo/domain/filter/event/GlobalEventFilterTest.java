package io.naryo.domain.filter.event;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterName;

class GlobalEventFilterTest extends AbstractEventFilterTest {

    @Override
    protected EventFilter createEventFilter(
            UUID id,
            FilterName name,
            UUID nodeId,
            EventFilterSpecification specification,
            Set<ContractEventStatus> statuses,
            FilterSyncState filterSyncState) {
        return new GlobalEventFilter(id, name, nodeId, specification, statuses, filterSyncState);
    }
}
