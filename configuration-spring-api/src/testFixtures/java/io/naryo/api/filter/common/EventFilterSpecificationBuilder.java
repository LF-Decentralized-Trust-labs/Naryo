package io.naryo.api.filter.common;

import java.util.Set;

import io.naryo.domain.common.event.EventName;
import io.naryo.domain.filter.event.CorrelationId;
import io.naryo.domain.filter.event.EventFilterSpecification;
import io.naryo.domain.filter.event.ParameterDefinition;
import io.naryo.domain.filter.event.parameter.AddressParameterDefinition;

public class EventFilterSpecificationBuilder {
    private EventName eventName;
    private CorrelationId correlationId;
    private Set<ParameterDefinition> parameters;

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

    public EventFilterSpecification build() {
        return new EventFilterSpecification(
                eventName != null ? eventName : new EventName("Test"),
                correlationId != null ? correlationId : new CorrelationId(0),
                parameters != null ? parameters : defaultParameters());
    }

    private Set<ParameterDefinition> defaultParameters() {
        return Set.of(new AddressParameterDefinition(0, false));
    }
}
