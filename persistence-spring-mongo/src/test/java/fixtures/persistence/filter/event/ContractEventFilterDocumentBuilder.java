package fixtures.persistence.filter.event;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.ContractEventFilterDocument;
import org.instancio.Instancio;

public class ContractEventFilterDocumentBuilder
    extends EventFilterDocumentBuilder<ContractEventFilterDocumentBuilder, ContractEventFilterDocument> {

    private String address;

    @Override
    public ContractEventFilterDocumentBuilder self() {
        return this;
    }

    @Override
    public ContractEventFilterDocument build() {
        return new ContractEventFilterDocument(
            this.getId(),
            this.getName(),
            this.getNodeId(),
            this.getScope(),
            this.getSpecification(),
            this.getStatuses(),
            this.getSync(),
            this.getVisibilityConfiguration(),
            this.getAddress()
        );
    }

    public ContractEventFilterDocumentBuilder withAddress(String contractAddress) {
        this.address = contractAddress;
        return this.self();
    }

    private String getAddress() {
        return this.address == null
            ? Instancio.create(String.class)
            : this.address;
    }

}
