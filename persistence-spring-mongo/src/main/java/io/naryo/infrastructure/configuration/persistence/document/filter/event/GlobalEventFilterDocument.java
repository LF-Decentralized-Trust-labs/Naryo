package io.naryo.infrastructure.configuration.persistence.document.filter.event;

import io.naryo.application.configuration.source.model.filter.event.global.GlobalEventFilterDescriptor;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.event.EventFilterScope;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.sync.FilterSyncDocument;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;

@TypeAlias("global_event_filter")
@Getter
public class GlobalEventFilterDocument extends EventFilterDocument implements GlobalEventFilterDescriptor {

    public GlobalEventFilterDocument(String id,
                                     String name,
                                     String nodeId,
                                     EventFilterScope scope,
                                     EventFilterSpecificationDocument specification,
                                     List<ContractEventStatus> statuses,
                                     @Nullable FilterSyncDocument sync,
                                     EventFilterVisibilityConfigurationDocument visibilityConfiguration) {
        super(id, name, nodeId, scope, specification, statuses, sync, visibilityConfiguration);
    }
}
