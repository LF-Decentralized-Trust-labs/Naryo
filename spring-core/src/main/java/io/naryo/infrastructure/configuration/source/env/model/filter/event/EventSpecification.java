package io.naryo.infrastructure.configuration.source.env.model.filter.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EventSpecification(
        @NotBlank String signature, @NotNull CorrelationId correlationId) {}
