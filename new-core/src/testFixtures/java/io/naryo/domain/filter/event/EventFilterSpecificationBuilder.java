package io.naryo.domain.filter.event;

import io.naryo.domain.filter.event.parameterdefinition.AddressParameterDefinitionBuilder;
import org.instancio.Instancio;

import java.util.List;

import static org.instancio.Select.field;

public class EventFilterSpecificationBuilder {

    private String eventName;
    private Integer correlationId;
    private List<ParameterDefinition> parameters;

    public EventFilterSpecification build() {
        return Instancio.of(EventFilterSpecification.class)
            .set(field(EventFilterSpecification::eventName), this.getEventName())
            .set(field(EventFilterSpecification::correlationId), this.getCorrelationId())
            .set(field(EventFilterSpecification::parameters), this.getParameters())
            .create();
    }

    public EventFilterSpecificationBuilder withEventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public EventFilterSpecificationBuilder withCorrelationId(int correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public EventFilterSpecificationBuilder withParameters(List<ParameterDefinition> parameters) {
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

    private List<ParameterDefinition> getParameters() {
        return this.parameters == null
            ? List.of(new AddressParameterDefinitionBuilder().build())
            : this.parameters;
    }
}
