package io.librevents.infrastructure.configuration.source.env.model.broadcaster;

import java.util.List;

import io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterConfigurationEntryProperties;
import io.librevents.infrastructure.configuration.source.env.model.broadcaster.target.BroadcasterTargetEntryProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record BroadcastingProperties(
        @Valid @NotNull List<BroadcasterConfigurationEntryProperties> configuration,
        @Valid @NotNull List<BroadcasterTargetEntryProperties> broadcasters) {}
