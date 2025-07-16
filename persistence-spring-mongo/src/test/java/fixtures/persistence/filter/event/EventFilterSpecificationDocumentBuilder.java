package fixtures.persistence.filter.event;

import io.naryo.infrastructure.configuration.persistence.document.filter.event.EventFilterSpecificationDocument;
import org.instancio.Instancio;

public class EventFilterSpecificationDocumentBuilder {

    private String eventName;
    private Integer correlationId;

    public EventFilterSpecificationDocument build() {
        return new EventFilterSpecificationDocument(
            this.getEventName(),
            this.getCorrelationId()
        );
    }

    public EventFilterSpecificationDocumentBuilder withEventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public EventFilterSpecificationDocumentBuilder withCorrelationId(int correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    private String getEventName() {
        return this.eventName == null
            ? Instancio.create(String.class)
            : this.eventName;
    }

    private int getCorrelationId() {
        return this.correlationId == null
            ? Instancio.create(Integer.class)
            : this.correlationId;
    }

}
