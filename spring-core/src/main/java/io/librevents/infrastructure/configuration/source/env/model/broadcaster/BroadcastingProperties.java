package io.librevents.infrastructure.configuration.source.env.model.broadcaster;

import java.util.List;

import io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterConfigurationEntryProperties;
import io.librevents.infrastructure.configuration.source.env.model.broadcaster.target.BroadcasterTargetEntryProperties;

public record BroadcastingProperties(
        List<BroadcasterConfigurationEntryProperties> configuration,
        List<BroadcasterTargetEntryProperties> broadcasters) {}
