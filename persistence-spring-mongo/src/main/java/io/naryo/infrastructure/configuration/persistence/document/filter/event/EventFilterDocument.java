package io.naryo.infrastructure.configuration.persistence.document.filter.event;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterType;
import io.naryo.domain.filter.event.EventFilterScope;
import io.naryo.infrastructure.configuration.persistence.document.filter.FilterDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.syncstate.SyncStateDocument;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public abstract class EventFilterDocument extends FilterDocument {

    @NotNull
    private EventFilterScope scope;

    @NotNull
    private EventFilterSpecificationDocument specification;

    @NotNull
    private List<ContractEventStatus> statuses;

    @Nullable
    private SyncStateDocument syncState;

    @NotNull
    private EventFilterVisibilityConfigurationDocument visibilityConfiguration;

    public EventFilterDocument(String id,
                               String name,
                               String nodeId,
                               EventFilterScope scope,
                               EventFilterSpecificationDocument specification,
                               List<ContractEventStatus> statuses,
                               @Nullable SyncStateDocument syncState,
                               EventFilterVisibilityConfigurationDocument visibilityConfiguration) {
        super(id, name, FilterType.EVENT, nodeId);
        this.scope = scope;
        this.specification = specification;
        this.statuses = statuses;
        this.syncState = syncState;
        this.visibilityConfiguration = visibilityConfiguration;
    }
}
