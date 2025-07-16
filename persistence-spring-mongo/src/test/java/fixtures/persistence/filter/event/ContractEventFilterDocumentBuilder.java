package fixtures.persistence.filter.event;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.ContractEventFilterDocument;
import org.instancio.Instancio;

public class ContractEventFilterDocumentBuilder
    extends EventFilterDocumentBuilder<ContractEventFilterDocumentBuilder, ContractEventFilterDocument> {

    private String contractAddress;

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
            this.getSyncState(),
            this.getVisibilityConfiguration(),
            this.getContractAddress()
        );
    }

    public ContractEventFilterDocumentBuilder withContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
        return this.self();
    }

    private String getContractAddress() {
        return this.contractAddress == null
            ? Instancio.create(String.class)
            : this.contractAddress;
    }
}
