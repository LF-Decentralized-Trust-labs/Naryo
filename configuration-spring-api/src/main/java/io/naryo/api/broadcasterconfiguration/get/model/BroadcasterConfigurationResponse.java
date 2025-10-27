package io.naryo.api.broadcasterconfiguration.get.model;

import java.util.Map;
import java.util.UUID;

import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Broadcaster configuration")
public record BroadcasterConfigurationResponse(
        UUID id,
        String type,
        BroadcasterCacheResponse cache,
        Map<String, Object> additionalProperties,
        String currentItemHash) {

    public static BroadcasterConfigurationResponse fromDomain(
            BroadcasterConfiguration config,
            Map<String, Object> additionalProperties,
            String itemHash) {

        return new BroadcasterConfigurationResponse(
                config.getId(),
                config.getType().getName(),
                BroadcasterCacheResponse.fromDomain(config.getCache()),
                additionalProperties,
                itemHash);
    }
}
