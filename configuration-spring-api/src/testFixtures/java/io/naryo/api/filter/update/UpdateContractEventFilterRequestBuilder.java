package io.naryo.api.filter.update;

import java.util.Set;

import io.naryo.api.filter.common.EventFilterSpecificationRequestBuilder;
import io.naryo.api.filter.common.EventFilterVisibilityConfigurationRequestBuilder;
import io.naryo.api.filter.common.FilterSyncStateRequestBuilder;
import io.naryo.api.filter.common.request.EventFilterSpecificationRequest;
import io.naryo.api.filter.common.request.EventFilterVisibilityConfigurationRequest;
import io.naryo.api.filter.common.request.FilterSyncStateRequest;
import io.naryo.api.filter.update.model.UpdateContractEventFilterRequest;
import io.naryo.domain.common.event.ContractEventStatus;
import org.instancio.Instancio;

public class UpdateContractEventFilterRequestBuilder
        extends UpdateFilterRequestBuilder<
                UpdateContractEventFilterRequestBuilder, UpdateContractEventFilterRequest> {

    private EventFilterSpecificationRequest specification;
    private Set<ContractEventStatus> statuses;
    private FilterSyncStateRequest filterSyncState;
    private EventFilterVisibilityConfigurationRequest visibilityConfiguration;
    private String contractAddress;

    public UpdateContractEventFilterRequestBuilder withSpecification(
            EventFilterSpecificationRequest specification) {
        this.specification = specification;
        return self();
    }

    public UpdateContractEventFilterRequestBuilder withStatuses(Set<ContractEventStatus> statuses) {
        this.statuses = statuses;
        return self();
    }

    public UpdateContractEventFilterRequestBuilder withFilterSyncState(
            FilterSyncStateRequest filterSyncState) {
        this.filterSyncState = filterSyncState;
        return self();
    }

    public UpdateContractEventFilterRequestBuilder withVisibilityConfiguration(
            EventFilterVisibilityConfigurationRequest visibilityConfiguration) {
        this.visibilityConfiguration = visibilityConfiguration;
        return self();
    }

    public UpdateContractEventFilterRequestBuilder withContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
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

    public String getContractAddress() {
        return this.contractAddress == null ? Instancio.create(String.class) : this.contractAddress;
    }

    @Override
    public UpdateContractEventFilterRequestBuilder self() {
        return this;
    }

    @Override
    public UpdateContractEventFilterRequest build() {
        return new UpdateContractEventFilterRequest(
                getName(),
                getNodeId(),
                getPrevItemHash(),
                getSpecification(),
                getStatuses(),
                getFilterSyncState(),
                getVisibilityConfiguration(),
                getContractAddress());
    }
}
