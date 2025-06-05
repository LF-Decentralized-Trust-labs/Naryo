package io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration;

import java.time.Duration;

import jakarta.validation.constraints.NotNull;

public record BroadcasterCacheProperties(@NotNull Duration expirationTime) {

    private static final Duration DEFAULT_EXPIRATION_TIME = Duration.ofMinutes(5);

    public BroadcasterCacheProperties(Duration expirationTime) {
        this.expirationTime = expirationTime != null ? expirationTime : DEFAULT_EXPIRATION_TIME;
    }

    public BroadcasterCacheProperties() {
        this(DEFAULT_EXPIRATION_TIME);
    }
}
