package io.naryo.api.filter.getAll.model;

import io.naryo.domain.filter.event.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Schema(description = "Event filter specification")
@Getter
public class EventFilterSpecificationResponse {

    private final @NotBlank String eventSignature;
    private final Integer correlationIdPosition;

    private EventFilterSpecificationResponse(String eventSignature, Integer correlationIdPosition) {
        this.eventSignature = eventSignature;
        this.correlationIdPosition = correlationIdPosition;
    }

    public static EventFilterSpecificationResponse fromDomain(
            EventFilterSpecification eventFilterSpecification) {
        return new EventFilterSpecificationResponse(
                eventFilterSpecification.getEventSignature(),
                eventFilterSpecification.correlationId() == null
                        ? null
                        : eventFilterSpecification.correlationId().position());
    }
}
