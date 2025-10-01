package io.naryo.infrastructure.configuration.persistence.document.broadcaster.configuration;

import java.time.Duration;

import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterCacheConfigurationDescriptor;
import io.naryo.domain.configuration.broadcaster.BroadcasterCache;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BroadcasterCacheConfiguration implements BroadcasterCacheConfigurationDescriptor {

    private static final Duration DEFAULT_EXPIRATION_TIME = Duration.ofMinutes(5);

    private @NotNull Duration expirationTime;

    public BroadcasterCacheConfiguration(Duration expirationTime) {
        this.expirationTime = (expirationTime != null) ? expirationTime : DEFAULT_EXPIRATION_TIME;
    }

    public BroadcasterCacheConfiguration() {
        this(DEFAULT_EXPIRATION_TIME);
    }

    public static BroadcasterCacheConfiguration fromDomain(BroadcasterCache source) {
        return new BroadcasterCacheConfiguration(source.expirationTime());
    }
}
