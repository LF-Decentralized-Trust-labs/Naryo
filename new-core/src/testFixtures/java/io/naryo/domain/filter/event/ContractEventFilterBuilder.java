package io.naryo.domain.filter.event;

import org.instancio.Instancio;

public class ContractEventFilterBuilder
    extends EventFilterBuilder<ContractEventFilterBuilder, ContractEventFilter> {

    private String contractAddress;

    @Override
    public ContractEventFilterBuilder self() {
        return this;
    }

    @Override
    public ContractEventFilter build() {
        return new ContractEventFilter(
            this.getId(),
            this.getName(),
            this.getNodeId(),
            this.getSpecification(),
            this.getStatuses(),
            this.getSyncState(),
            this.getVisibilityConfiguration(),
            this.getContractAddress()
        );
    }

    public ContractEventFilterBuilder withContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
        return this.self();
    }

    private String getContractAddress() {
        return this.contractAddress == null
            ? Instancio.create(String.class)
            : this.contractAddress;
    }
}
