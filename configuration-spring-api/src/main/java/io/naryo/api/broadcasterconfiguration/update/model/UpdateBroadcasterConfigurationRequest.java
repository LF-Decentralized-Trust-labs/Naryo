package io.naryo.api.broadcasterconfiguration.update.model;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;
import io.naryo.api.broadcasterconfiguration.common.model.BroadcasterCacheConfigurationRequest;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterCacheConfigurationDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterConfigurationDescriptor;
import io.naryo.domain.broadcaster.BroadcasterType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class UpdateBroadcasterConfigurationRequest implements BroadcasterConfigurationDescriptor {

    private @NotNull UUID id;

    private @NotNull String type;

    private BroadcasterCacheConfigurationRequest cache;

    private Map<String, Object> additionalProperties;

    private @NotBlank String prevItemHash;

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
    public <T extends BroadcasterCacheConfigurationDescriptor> Optional<T> getCache() {
        return Optional.ofNullable((T) cache);
    }

    @Override
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties == null ? Map.of() : additionalProperties;
    }

    @Override
    public void setCache(BroadcasterCacheConfigurationDescriptor cache) {
        this.cache = (BroadcasterCacheConfigurationRequest) cache;
    }

    @Override
    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
