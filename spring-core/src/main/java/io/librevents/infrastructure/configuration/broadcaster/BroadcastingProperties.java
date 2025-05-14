package io.librevents.infrastructure.configuration.broadcaster;

import java.util.List;

import io.librevents.infrastructure.configuration.broadcaster.configuration.BroadcasterConfigurationEntryProperties;
import io.librevents.infrastructure.configuration.broadcaster.target.BroadcasterTargetEntryProperties;

public record BroadcastingProperties(
        List<BroadcasterConfigurationEntryProperties> configuration,
        List<BroadcasterTargetEntryProperties> broadcasters) {}
