package io.naryo.infrastructure.configuration.persistence.document.filter.event;

import java.util.Optional;

import io.naryo.application.configuration.source.model.filter.event.EventSpecificationDescriptor;
import jakarta.annotation.Nullable;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Setter
public class EventSpecificationDocument implements EventSpecificationDescriptor {

    @Nullable private String signature;

    @Nullable private Integer correlationId;

    public EventSpecificationDocument(String signature, Integer correlationId) {
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
