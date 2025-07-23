package fixtures.persistence.filter.event;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.GlobalEventFilterDocument;

public class GlobalEventFilterDocumentBuilder
        extends EventFilterDocumentBuilder<
                GlobalEventFilterDocumentBuilder, GlobalEventFilterDocument> {

    @Override
    public GlobalEventFilterDocumentBuilder self() {
        return this;
    }

    @Override
    public GlobalEventFilterDocument build() {
        return new GlobalEventFilterDocument(
                this.getId(),
                this.getName(),
                this.getNodeId(),
                this.getSpecification(),
                this.getStatuses(),
                this.getSync(),
                this.getVisibility());
    }
}
