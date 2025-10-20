package io.naryo.api.filter.create;

import java.util.Set;

import io.naryo.api.filter.common.EventFilterSpecificationRequestBuilder;
import io.naryo.api.filter.common.EventFilterVisibilityConfigurationRequestBuilder;
import io.naryo.api.filter.common.FilterSyncStateRequestBuilder;
import io.naryo.api.filter.common.request.EventFilterSpecificationRequest;
import io.naryo.api.filter.common.request.EventFilterVisibilityConfigurationRequest;
import io.naryo.api.filter.common.request.FilterSyncStateRequest;
import io.naryo.api.filter.create.model.CreateGlobalEventFilterRequest;
import io.naryo.domain.common.event.ContractEventStatus;
import org.instancio.Instancio;

public class CreateGlobalEventFilterRequestBuilder
        extends CreateFilterRequestBuilder<
                CreateGlobalEventFilterRequestBuilder, CreateGlobalEventFilterRequest> {

    private EventFilterSpecificationRequest specification;
    private Set<ContractEventStatus> statuses;
    private FilterSyncStateRequest filterSyncState;
    private EventFilterVisibilityConfigurationRequest visibilityConfiguration;

    public CreateGlobalEventFilterRequestBuilder withSpecification(
            EventFilterSpecificationRequest specification) {
        this.specification = specification;
        return self();
    }

    public CreateGlobalEventFilterRequestBuilder withStatuses(Set<ContractEventStatus> statuses) {
        this.statuses = statuses;
        return self();
    }

    public CreateGlobalEventFilterRequestBuilder withFilterSyncState(
            FilterSyncStateRequest filterSyncState) {
        this.filterSyncState = filterSyncState;
        return self();
    }

    public CreateGlobalEventFilterRequestBuilder withVisibilityConfiguration(
            EventFilterVisibilityConfigurationRequest visibilityConfiguration) {
        this.visibilityConfiguration = visibilityConfiguration;
        return self();
    }

    public EventFilterSpecificationRequest getSpecification() {
        return this.specification == null
                ? new EventFilterSpecificationRequestBuilder().build()
                : this.specification;
    }

    public Set<ContractEventStatus> getStatuses() {
        return this.statuses == null
                ? Instancio.ofSet(ContractEventStatus.class).size(2).create()
                : this.statuses;
    }

    public FilterSyncStateRequest getFilterSyncState() {
        return this.filterSyncState == null
                ? new FilterSyncStateRequestBuilder().build()
                : this.filterSyncState;
    }

    public EventFilterVisibilityConfigurationRequest getVisibilityConfiguration() {
        return this.visibilityConfiguration == null
                ? new EventFilterVisibilityConfigurationRequestBuilder().build()
                : this.visibilityConfiguration;
    }

    @Override
    public CreateGlobalEventFilterRequestBuilder self() {
        return this;
    }

    @Override
    public CreateGlobalEventFilterRequest build() {
        return new CreateGlobalEventFilterRequest(
                getName(),
                getNodeId(),
                getSpecification(),
                getStatuses(),
                getFilterSyncState(),
                getVisibilityConfiguration());
    }
}
