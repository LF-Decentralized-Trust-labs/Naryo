package io.naryo.infrastructure.configuration.source.env.model.filter.event.global;

import java.util.Set;
import java.util.UUID;

import io.naryo.application.configuration.source.model.filter.event.global.GlobalEventFilterDescriptor;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.EventFilterProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.EventSpecification;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.FilterSyncProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.visibility.EventFilterVisibilityProperties;

public final class GlobalEventFilterProperties extends EventFilterProperties
        implements GlobalEventFilterDescriptor {

    public GlobalEventFilterProperties(
            UUID id,
            String name,
            UUID nodeId,
            EventSpecification specification,
            Set<ContractEventStatus> statuses,
            FilterSyncProperties sync,
            EventFilterVisibilityProperties visibility) {
        super(id, name, nodeId, specification, statuses, sync, visibility);
    }
}
