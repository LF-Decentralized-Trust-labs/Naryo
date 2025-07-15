package io.naryo.infrastructure.configuration.persistence.document.filter.event;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.event.EventFilterScope;
import io.naryo.infrastructure.configuration.persistence.document.filter.FilterDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.syncstate.SyncStatePropertiesDocument;
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
    private SyncStatePropertiesDocument syncState;

    @NotNull
    private EventFilterVisibilityConfigurationDocument visibilityConfiguration;

}
