package io.naryo.api.filter.common.request;

import io.naryo.domain.filter.event.CorrelationId;
import io.naryo.domain.filter.event.EventFilterSpecification;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EventFilterSpecificationRequest {

    private final @NotBlank String eventSignature;
    private final Integer correlationIdPosition;

    public EventFilterSpecificationRequest(String eventSignature, Integer correlationIdPosition) {
        this.eventSignature = eventSignature;
        this.correlationIdPosition = correlationIdPosition;
    }

    public EventFilterSpecification toDomain() {
        CorrelationId correlationId =
                correlationIdPosition != null ? new CorrelationId(correlationIdPosition) : null;
        return new EventFilterSpecification(eventSignature, correlationId);
    }
}
