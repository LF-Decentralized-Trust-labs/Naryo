package io.naryo.api.broadcasterconfiguration.common.model;

import java.time.Duration;

import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterCacheConfigurationDescriptor;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class BroadcasterCacheConfigurationRequest
        implements BroadcasterCacheConfigurationDescriptor {

    @NotNull private Duration expirationTime;

    @Override
    public Duration getExpirationTime() {
        return expirationTime;
    }
}
