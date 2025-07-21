package io.naryo.infrastructure.configuration.persistence.document.filter.event;

import java.util.Set;

import io.naryo.application.configuration.source.model.filter.event.global.GlobalEventFilterDescriptor;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.event.EventFilterScope;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.sync.FilterSyncDocument;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("global_event_filter")
@Getter
public class GlobalEventFilterDocument extends EventFilterDocument
        implements GlobalEventFilterDescriptor {

    public GlobalEventFilterDocument(
            String id,
            String name,
            String nodeId,
            EventFilterScope scope,
            EventSpecificationDocument specification,
            Set<ContractEventStatus> statuses,
            FilterSyncDocument sync,
            FilterVisibilityDocument visibility) {
        super(id, name, nodeId, scope, specification, statuses, sync, visibility);
    }
}
