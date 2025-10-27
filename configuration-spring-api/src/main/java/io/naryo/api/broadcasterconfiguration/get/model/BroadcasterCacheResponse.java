package io.naryo.api.broadcasterconfiguration.get.model;

import java.time.Duration;

import io.naryo.domain.configuration.broadcaster.BroadcasterCache;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Broadcaster cache")
public record BroadcasterCacheResponse(Duration expirationTime) {

    public static BroadcasterCacheResponse fromDomain(BroadcasterCache broadcasterCache) {
        return new BroadcasterCacheResponse(broadcasterCache.expirationTime());
    }
}
