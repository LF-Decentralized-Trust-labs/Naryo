package io.naryo.infrastructure.configuration.persistence.entity.broadcaster.configuration;

import java.time.Duration;

import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterCacheConfigurationDescriptor;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Embeddable
@Getter
public class BroadcasterCacheEntity implements BroadcasterCacheConfigurationDescriptor {

    private static final Duration DEFAULT_EXPIRATION_TIME = Duration.ofMinutes(5);

    private @NotNull Duration expirationTime;

    public BroadcasterCacheEntity(Duration expirationTime) {
        this.expirationTime = (expirationTime != null) ? expirationTime : DEFAULT_EXPIRATION_TIME;
    }

    public BroadcasterCacheEntity() {
        this(DEFAULT_EXPIRATION_TIME);
    }
}
