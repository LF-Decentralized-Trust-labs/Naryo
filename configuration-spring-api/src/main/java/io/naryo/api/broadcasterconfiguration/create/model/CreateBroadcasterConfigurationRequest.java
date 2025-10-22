package io.naryo.api.broadcasterconfiguration.create.model;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.naryo.api.broadcasterconfiguration.common.model.BroadcasterCacheConfigurationRequest;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterCacheConfigurationDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterConfigurationDescriptor;
import io.naryo.domain.broadcaster.BroadcasterType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
public class CreateBroadcasterConfigurationRequest implements BroadcasterConfigurationDescriptor {

    private @JsonIgnore @EqualsAndHashCode.Exclude UUID id;

    private @NotBlank String type;

    private BroadcasterCacheConfigurationRequest cache;

    private @NotNull Map<String, Object> additionalProperties;

    public CreateBroadcasterConfigurationRequest(
            String type,
            BroadcasterCacheConfigurationRequest cache,
            Map<String, Object> additionalProperties) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.cache = cache;
        this.additionalProperties = additionalProperties;
    }

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

    public void ensureId() {
        this.id = UUID.randomUUID();
    }
}
