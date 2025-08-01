package io.naryo.infrastructure.configuration.persistence.entity.filter.event;

import java.util.Set;
import java.util.UUID;

import io.naryo.application.configuration.source.model.filter.event.global.GlobalEventFilterDescriptor;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.infrastructure.configuration.persistence.entity.filter.event.sync.FilterSyncEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("event_global")
@NoArgsConstructor
public class GlobalEventFilterEntity extends EventFilterEntity
        implements GlobalEventFilterDescriptor {

    public GlobalEventFilterEntity(
            UUID id,
            String name,
            UUID nodeId,
            EventSpecification specification,
            Set<ContractEventStatus> statuses,
            FilterSyncEntity sync,
            FilterVisibility visibility) {
        super(id, name, nodeId, specification, statuses, sync, visibility);
    }
}
