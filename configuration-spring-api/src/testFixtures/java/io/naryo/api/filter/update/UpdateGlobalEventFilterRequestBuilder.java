package io.naryo.api.filter.update;

import io.naryo.api.filter.common.EventFilterSpecificationRequestBuilder;
import io.naryo.api.filter.common.EventFilterVisibilityConfigurationRequestBuilder;
import io.naryo.api.filter.common.FilterSyncStateRequestBuilder;
import io.naryo.api.filter.common.request.EventFilterSpecificationRequest;
import io.naryo.api.filter.common.request.EventFilterVisibilityConfigurationRequest;
import io.naryo.api.filter.common.request.FilterSyncStateRequest;
import io.naryo.api.filter.update.model.UpdateGlobalEventFilterRequest;
import io.naryo.domain.common.event.ContractEventStatus;
import org.instancio.Instancio;

import java.util.Set;

public class UpdateGlobalEventFilterRequestBuilder extends UpdateFilterRequestBuilder<UpdateGlobalEventFilterRequestBuilder, UpdateGlobalEventFilterRequest> {

    private EventFilterSpecificationRequest specification;
    private Set<ContractEventStatus> statuses;
    private FilterSyncStateRequest filterSyncState;
    private EventFilterVisibilityConfigurationRequest visibilityConfiguration;

    public UpdateGlobalEventFilterRequestBuilder withSpecification(EventFilterSpecificationRequest specification) {
        this.specification = specification;
        return self();
    }

    public UpdateGlobalEventFilterRequestBuilder withStatuses(Set<ContractEventStatus> statuses) {
        this.statuses = statuses;
        return self();
    }

    public UpdateGlobalEventFilterRequestBuilder withFilterSyncState(FilterSyncStateRequest filterSyncState) {
        this.filterSyncState = filterSyncState;
        return self();
    }

    public UpdateGlobalEventFilterRequestBuilder withVisibilityConfiguration(EventFilterVisibilityConfigurationRequest visibilityConfiguration) {
        this.visibilityConfiguration = visibilityConfiguration;
        return self();
    }

    public EventFilterSpecificationRequest getSpecification() {
        return this.specification == null ? new EventFilterSpecificationRequestBuilder().build() : this.specification;
    }

    public Set<ContractEventStatus> getStatuses() {
        return this.statuses == null ? Instancio.ofSet(ContractEventStatus.class).size(2).create() : this.statuses;
    }

    public FilterSyncStateRequest getFilterSyncState() {
        return this.filterSyncState == null ? new FilterSyncStateRequestBuilder().build() : this.filterSyncState;
    }

    public EventFilterVisibilityConfigurationRequest getVisibilityConfiguration() {
        return this.visibilityConfiguration == null ? new EventFilterVisibilityConfigurationRequestBuilder().build() : this.visibilityConfiguration;
    }

    @Override
    public UpdateGlobalEventFilterRequestBuilder self() {
        return this;
    }

    @Override
    public UpdateGlobalEventFilterRequest build() {
        return new UpdateGlobalEventFilterRequest(
            getName(),
            getNodeId(),
            getPrevItemHash(),
            getSpecification(),
            getStatuses(),
            getFilterSyncState(),
            getVisibilityConfiguration()
        );
    }
}
