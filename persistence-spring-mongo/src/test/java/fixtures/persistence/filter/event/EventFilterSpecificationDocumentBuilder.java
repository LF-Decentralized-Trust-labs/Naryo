package fixtures.persistence.filter.event;

import fixtures.persistence.filter.event.parameterdefinition.AddressParameterDefinitionDocumentBuilder;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.EventFilterSpecificationDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.parameterdefinition.ParameterDefinitionDocument;
import org.instancio.Instancio;

import java.util.List;

public class EventFilterSpecificationDocumentBuilder {

    private String eventName;
    private Integer correlationId;
    private List<ParameterDefinitionDocument> parameters;

    public EventFilterSpecificationDocument build() {
        return new EventFilterSpecificationDocument(
            this.getEventName(),
            this.getCorrelationId(),
            this.getParameters()
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

    public EventFilterSpecificationDocumentBuilder withParameters(List<ParameterDefinitionDocument> parameters) {
        this.parameters = parameters;
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

    private List<ParameterDefinitionDocument> getParameters() {
        return this.parameters == null
            ? List.of(new AddressParameterDefinitionDocumentBuilder().build())
            : this.parameters;
    }
}
