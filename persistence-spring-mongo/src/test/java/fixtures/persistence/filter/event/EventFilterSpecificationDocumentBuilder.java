package fixtures.persistence.filter.event;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.EventSpecificationDocument;
import org.instancio.Instancio;

public class EventFilterSpecificationDocumentBuilder {

    private String signature;
    private Integer correlationId;

    public EventSpecificationDocument build() {
        return new EventSpecificationDocument(
            this.getSignature(),
            this.getCorrelationId()
        );
    }

    public EventFilterSpecificationDocumentBuilder withSignature(String signature) {
        this.signature = signature;
        return this;
    }

    public EventFilterSpecificationDocumentBuilder withCorrelationId(int correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    private String getSignature() {
        return this.signature == null
            ? Instancio.create(String.class)
            : this.signature;
    }

    private int getCorrelationId() {
        return this.correlationId == null
            ? Instancio.create(Integer.class)
            : this.correlationId;
    }

}
