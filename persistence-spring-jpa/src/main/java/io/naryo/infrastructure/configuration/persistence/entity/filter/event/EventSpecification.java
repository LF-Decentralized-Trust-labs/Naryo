package io.naryo.infrastructure.configuration.persistence.entity.filter.event;

import java.util.Optional;

import io.naryo.application.configuration.source.model.filter.event.EventSpecificationDescriptor;
import io.naryo.domain.filter.event.EventFilterSpecification;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Setter
@NoArgsConstructor
public class EventSpecification implements EventSpecificationDescriptor {

    @Column(name = "signature")
    private @Nullable String signature;

    @Column(name = "correlation_id")
    private @Nullable Integer correlationId;

    public EventSpecification(String signature, Integer correlationId) {
        this.signature = signature;
        this.correlationId = correlationId;
    }

    @Override
    public Optional<String> getSignature() {
        return Optional.ofNullable(signature);
    }

    @Override
    public Optional<Integer> getCorrelationId() {
        return Optional.ofNullable(correlationId);
    }

    public static EventSpecification fromDomain(EventFilterSpecification source) {
        return new EventSpecification(
                source.getEventSignature(), source.correlationId().position());
    }
}
