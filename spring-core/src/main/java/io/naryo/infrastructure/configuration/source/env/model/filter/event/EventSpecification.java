package io.naryo.infrastructure.configuration.source.env.model.filter.event;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record EventSpecification(
        @NotBlank String signature, @Nullable CorrelationId correlationId) {}
