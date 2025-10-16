package io.naryo.api.filter.create;

import io.naryo.api.filter.common.EventFilterSpecificationRequestBuilder;
import io.naryo.api.filter.common.FilterSyncStateRequestBuilder;
import io.naryo.api.filter.common.request.EventFilterSpecificationRequest;
import io.naryo.api.filter.common.request.FilterSyncStateRequest;
import io.naryo.api.filter.create.model.CreateContractEventFilterRequest;
import io.naryo.domain.common.event.ContractEventStatus;
import org.instancio.Instancio;

import java.util.Set;

public class CreateContractEventFilterRequestBuilder extends CreateFilterRequestBuilder<CreateContractEventFilterRequestBuilder, CreateContractEventFilterRequest> {

    private EventFilterSpecificationRequest specification;
    private Set<ContractEventStatus> statuses;
    private FilterSyncStateRequest filterSyncState;
    private String contractAddress;

    public CreateContractEventFilterRequestBuilder withSpecification(EventFilterSpecificationRequest specification) {
        this.specification = specification;
        return self();
    }

    public CreateContractEventFilterRequestBuilder withStatuses(Set<ContractEventStatus> statuses) {
        this.statuses = statuses;
        return self();
    }

    public CreateContractEventFilterRequestBuilder withFilterSyncState(FilterSyncStateRequest filterSyncState) {
        this.filterSyncState = filterSyncState;
        return self();
    }

    public CreateContractEventFilterRequestBuilder withContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
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

    public String getContractAddress() {
        return this.contractAddress == null ? Instancio.create(String.class) : this.contractAddress;
    }

    @Override
    public CreateContractEventFilterRequestBuilder self() {
        return this;
    }

    @Override
    public CreateContractEventFilterRequest build() {
        return new CreateContractEventFilterRequest(
            getName(),
            getNodeId(),
            getSpecification(),
            getStatuses(),
            getFilterSyncState(),
            getContractAddress()
        );
    }
}
