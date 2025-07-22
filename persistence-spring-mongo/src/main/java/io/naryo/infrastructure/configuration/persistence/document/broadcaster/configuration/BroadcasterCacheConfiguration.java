package io.naryo.infrastructure.configuration.persistence.document.broadcaster.configuration;

import java.time.Duration;

import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterCacheConfigurationDescriptor;
import jakarta.validation.constraints.NotNull;

public class BroadcasterCacheConfiguration implements BroadcasterCacheConfigurationDescriptor {

    private static final Duration DEFAULT_EXPIRATION_TIME = Duration.ofMinutes(5);

    @NotNull private Duration expirationTime;

    public BroadcasterCacheConfiguration(Duration expirationTime) {
        this.expirationTime = (expirationTime != null) ? expirationTime : DEFAULT_EXPIRATION_TIME;
    }

    public BroadcasterCacheConfiguration() {
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
