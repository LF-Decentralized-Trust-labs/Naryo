package io.naryo.infrastructure.configuration.source.env.model.broadcaster.configuration;

import java.time.Duration;

import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterCacheConfigurationDescriptor;
import jakarta.validation.constraints.NotNull;

public class BroadcasterCacheProperties implements BroadcasterCacheConfigurationDescriptor {

    private static final Duration DEFAULT_EXPIRATION_TIME = Duration.ofMinutes(5);

    private @NotNull Duration expirationTime;

    public BroadcasterCacheProperties(Duration expirationTime) {
        this.expirationTime = expirationTime != null ? expirationTime : DEFAULT_EXPIRATION_TIME;
    }

    public BroadcasterCacheProperties() {
        this(DEFAULT_EXPIRATION_TIME);
    }

    @Override
    public Duration getExpirationTime() {
        return expirationTime;
    }

    @Override
    public void setExpirationTime(Duration expirationTime) {
        this.expirationTime = expirationTime;
    }
}
