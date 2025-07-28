package io.naryo.domain.filter.event;

import java.util.Set;

import io.naryo.domain.DomainBuilder;
import io.naryo.domain.common.event.EventName;
import io.naryo.domain.filter.event.parameterdefinition.AddressParameterDefinitionBuilder;
import org.instancio.Instancio;

public class EventFilterSpecificationBuilder
        implements DomainBuilder<EventFilterSpecificationBuilder, EventFilterSpecification> {

    private EventName eventName;
    private CorrelationId correlationId;
    private Set<ParameterDefinition> parameters;

    @Override
    public EventFilterSpecificationBuilder self() {
        return this;
    }

    @Override
    public EventFilterSpecification build() {
        return new EventFilterSpecification(
                this.getEventName(), this.getCorrelationId(), this.getParameters());
    }

    public EventFilterSpecificationBuilder withEventName(EventName eventName) {
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

    private EventName getEventName() {
        return this.eventName == null ? Instancio.create(EventName.class) : this.eventName;
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
