package io.naryo.infrastructure.configuration.source.env.model.broadcaster;

import java.util.List;

import io.naryo.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterConfigurationEntryProperties;
import io.naryo.infrastructure.configuration.source.env.model.broadcaster.target.BroadcasterEntryProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record BroadcastingProperties(
        @Valid @NotNull List<BroadcasterConfigurationEntryProperties> configuration,
        @Valid @NotNull List<BroadcasterEntryProperties> broadcasters) {}
