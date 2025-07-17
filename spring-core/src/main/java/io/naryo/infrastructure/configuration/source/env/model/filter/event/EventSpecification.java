package io.naryo.infrastructure.configuration.source.env.model.filter.event;

import io.naryo.application.configuration.source.model.filter.event.EventSpecificationDescriptor;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public final class EventSpecification implements EventSpecificationDescriptor {

    private Optional<String> signature;
    private Optional<Integer> correlationId;

    public EventSpecification() {
        this.signature = Optional.empty();
        this.correlationId = Optional.empty();
    }

    public EventSpecification(Optional<String> signature,
                              Optional<Integer> correlationId) {
        this.signature = signature;
        this.correlationId = correlationId;
    }

}
