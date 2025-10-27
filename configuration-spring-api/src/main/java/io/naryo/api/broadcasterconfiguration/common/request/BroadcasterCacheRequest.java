package io.naryo.api.broadcasterconfiguration.common.request;

import java.time.Duration;

import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterCacheConfigurationDescriptor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Schema(description = "Broadcaster cache request")
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class BroadcasterCacheRequest implements BroadcasterCacheConfigurationDescriptor {

    @NotNull private Duration expirationTime;
}
