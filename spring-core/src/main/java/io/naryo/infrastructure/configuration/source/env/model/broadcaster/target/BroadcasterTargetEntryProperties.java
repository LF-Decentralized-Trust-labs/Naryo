package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import java.util.UUID;

import io.naryo.domain.broadcaster.BroadcasterTargetType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BroadcasterTargetEntryProperties(
        @NotNull UUID configurationId,
        @NotNull BroadcasterTargetType type,
        @NotBlank String destination,
        @Valid @NotNull BroadcasterTargetAdditionalProperties configuration) {}
