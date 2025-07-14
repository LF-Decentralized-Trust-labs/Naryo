package io.naryo.infrastructure.configuration.source.env.model.broadcaster.configuration;

import java.util.Map;
import java.util.UUID;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterCacheConfigurationDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterConfigurationDescriptor;
import io.naryo.domain.broadcaster.BroadcasterType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public final class BroadcasterConfigurationEntryProperties
        implements BroadcasterConfigurationDescriptor {

    private final @NotNull UUID id;
    private final @NotNull BroadcasterType type;
    private @Valid @NotNull BroadcasterCacheProperties cache;
    private @NotNull Map<String, Object> additionalProperties;
    private @NotNull ConfigurationSchema propertiesSchema;

    public BroadcasterConfigurationEntryProperties(
            UUID id,
            BroadcasterType type,
            BroadcasterCacheProperties cache,
            Map<String, Object> additionalProperties,
            ConfigurationSchema propertiesSchema) {
        this.id = id;
        this.type = type;
        this.cache = cache;
        this.additionalProperties = additionalProperties;
        this.propertiesSchema = propertiesSchema;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public BroadcasterType getType() {
        return type;
    }

    @Override
    public BroadcasterCacheConfigurationDescriptor getCache() {
        return cache;
    }

    @Override
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @Override
    public ConfigurationSchema getPropertiesSchema() {
        return propertiesSchema;
    }

    @Override
    public void setCache(BroadcasterCacheConfigurationDescriptor cache) {
        this.cache = (BroadcasterCacheProperties) cache;
    }

    @Override
    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    @Override
    public void setPropertiesSchema(ConfigurationSchema propertiesSchema) {
        this.propertiesSchema = propertiesSchema;
    }
}
