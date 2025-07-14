package io.naryo.infrastructure.configuration.source.env.model.filter.event;

import io.naryo.application.configuration.source.model.filter.event.EventSpecificationDescriptor;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public final class EventSpecification implements EventSpecificationDescriptor {

    private @Getter @Setter @NotBlank String signature;
    private @Getter @Setter @Nullable Integer correlationId;
}
