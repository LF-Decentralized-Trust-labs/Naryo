package io.naryo.api.broadcasterconfiguration.get.model;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;

public record BroadcasterConfigurationResponse(
        UUID id,
        String type,
        BroadcasterCacheResponse cache,
        Map<String, Object> additionalProperties,
        String itemHash) {

    public static BroadcasterConfigurationResponse map(
            BroadcasterConfiguration config,
            Map<String, Object> additionalProperties,
            String itemHash) {
        Objects.requireNonNull(config, "config cannot be null");

        BroadcasterCacheResponse cacheResponse =
                Optional.ofNullable(config.getCache())
                        .map(c -> new BroadcasterCacheResponse(c.expirationTime()))
                        .orElse(null);

        return new BroadcasterConfigurationResponse(
                config.getId(),
                config.getType().getName(),
                cacheResponse,
                additionalProperties,
                itemHash);
    }

    public record BroadcasterCacheResponse(Duration expirationTime) {}
}
