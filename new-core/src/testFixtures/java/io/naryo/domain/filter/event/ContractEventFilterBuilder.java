package io.naryo.domain.filter.event;

import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public class ContractEventFilterBuilder
    extends EventFilterBuilder<ContractEventFilterBuilder, ContractEventFilter> {

    private String contractAddress;

    @Override
    public ContractEventFilterBuilder self() {
        return this;
    }

    @Override
    public ContractEventFilter build() {
        InstancioApi<ContractEventFilter> builder = Instancio.of(ContractEventFilter.class);

        return super.buildBase(builder)
            .set(field(ContractEventFilter::getContractAddress), this.getContractAddress())
            .create();
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
