package io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration;

import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BroadcasterConfigurationEntryProperties(
        @NotNull UUID id,
        @NotBlank String type,
        @Valid @NotNull BroadcasterCacheProperties cache,
        @Valid @NotNull BroadcasterConfigurationAdditionalProperties configuration) {}
