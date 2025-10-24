package io.naryo.api.filter.common;

import io.naryo.api.filter.common.request.ContractEventFilterRequest;
import org.instancio.Instancio;

public class ContractEventFilterRequestBuilder
        extends EventFilterRequestBuilder<
                ContractEventFilterRequestBuilder, ContractEventFilterRequest> {

    private String contractAddress;

    public ContractEventFilterRequestBuilder withContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
        return self();
    }

    public String getContractAddress() {
        return this.contractAddress == null ? Instancio.create(String.class) : this.contractAddress;
    }

    @Override
    public ContractEventFilterRequestBuilder self() {
        return this;
    }

    @Override
    public ContractEventFilterRequest build() {
        return new ContractEventFilterRequest(
                getName(),
                getNodeId(),
                getSpecification(),
                getStatuses(),
                getFilterSyncState(),
                getVisibilityConfiguration(),
                getContractAddress());
    }
}
