package io.naryo.api.broadcasterconfiguration.common.request;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterCacheConfigurationDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterConfigurationDescriptor;
import io.naryo.domain.broadcaster.BroadcasterType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Schema(description = "Broadcaster configuration request")
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class BroadcasterConfigurationRequest implements BroadcasterConfigurationDescriptor {

    private @NotNull UUID id;

    private @NotNull String type;

    private BroadcasterCacheRequest cache;

    private Map<String, Object> additionalProperties;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public BroadcasterType getType() {
        return () -> type;
    }

    @JsonGetter("type")
    @EqualsAndHashCode.Include
    public String getTypeJson() {
        return type;
    }

    @Override
    public Optional<BroadcasterCacheRequest> getCache() {
        return Optional.ofNullable(cache);
    }

    @Override
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties == null ? Map.of() : additionalProperties;
    }

    @Override
    public void setCache(BroadcasterCacheConfigurationDescriptor cache) {
        this.cache = (BroadcasterCacheRequest) cache;
    }

    @Override
    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
