package io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration;

import java.time.Duration;

public record BroadcasterCacheProperties(Duration expirationTime) {}
