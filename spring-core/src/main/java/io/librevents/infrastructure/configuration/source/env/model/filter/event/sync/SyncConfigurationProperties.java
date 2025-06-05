package io.librevents.infrastructure.configuration.source.env.model.filter.event.sync;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record SyncConfigurationProperties(
        @NotNull SyncType type,
        @Valid @NotNull SyncConfigurationAdditionalProperties configuration) {}
