package io.naryo.infrastructure.configuration.persistence.document.filter.event;

import io.naryo.application.configuration.source.model.filter.event.EventSpecificationDescriptor;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventFilterSpecificationDocument implements EventSpecificationDescriptor {

    @NotNull
    private String signature;

    @NotNull
    private Integer correlationId;

    public EventFilterSpecificationDocument(String signature, Integer correlationId) {
        this.signature = signature;
        this.correlationId = correlationId;
    }

}
