package io.naryo.infrastructure.configuration.source.env.model.filter.event;

import java.util.Optional;

import io.naryo.application.configuration.source.model.filter.event.EventSpecificationDescriptor;
import jakarta.annotation.Nullable;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public final class EventSpecification implements EventSpecificationDescriptor {

    private @Nullable String signature;
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
}
