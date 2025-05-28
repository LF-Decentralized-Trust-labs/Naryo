package io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration;

import java.util.UUID;

public record BroadcasterConfigurationEntryProperties(
        UUID id,
        String type,
        BroadcasterCacheProperties cache,
        BroadcasterConfigurationAdditionalProperties configuration) {}
