package io.naryo.infrastructure.configuration.persistence.document.filter.event;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.event.EventFilterScope;
import io.naryo.infrastructure.configuration.persistence.document.filter.FilterPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.syncstate.SyncStatePropertiesDocument;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public abstract class EventFilterPropertiesDocument extends FilterPropertiesDocument {

    @NotNull
    private EventFilterScope scope;

    @NotNull
    private EventFilterSpecificationPropertiesDocument specification;

    @NotNull
    private List<ContractEventStatus> statuses;

    private SyncStatePropertiesDocument syncState;

    @NotNull
    private EventFilterVisibilityConfigurationPropertiesDocument visibilityConfiguration;

}
