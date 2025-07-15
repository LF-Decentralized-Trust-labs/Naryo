package io.naryo.domain.filter.event;

import io.naryo.domain.filter.event.parameterdefinition.AddressParameterDefinitionBuilder;
import org.instancio.Instancio;

import java.util.Set;

import static org.instancio.Select.field;

public class EventFilterSpecificationBuilder {

    private String eventName;
    private CorrelationId correlationId;
    private Set<ParameterDefinition> parameters;

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

    public EventFilterSpecificationBuilder withCorrelationId(CorrelationId correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public EventFilterSpecificationBuilder withParameters(Set<ParameterDefinition> parameters) {
        this.parameters = parameters;
        return this;
    }

    private String getEventName() {
        return this.eventName == null
            ? Instancio.create(String.class)
            : this.eventName;
    }

    private CorrelationId getCorrelationId() {
        return this.correlationId == null
            ? Instancio.create(CorrelationId.class)
            : this.correlationId;
    }

    private Set<ParameterDefinition> getParameters() {
        return this.parameters == null
            ? Set.of(new AddressParameterDefinitionBuilder().build())
            : this.parameters;
    }
}
