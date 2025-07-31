package io.naryo.infrastructure.configuration.persistence.entity.broadcaster.configuration;

import java.time.Duration;

import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterCacheConfigurationDescriptor;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class BroadcasterCacheEntity implements BroadcasterCacheConfigurationDescriptor {

    private @NotNull Duration expirationTime;

    public BroadcasterCacheEntity(Duration expirationTime) {
        this.expirationTime = expirationTime;
    }
}
