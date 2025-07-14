package io.naryo.application.configuration.source.model.broadcaster.configuration;

import java.util.Map;
import java.util.UUID;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.broadcaster.BroadcasterType;

public interface BroadcasterConfigurationDescriptor
        extends MergeableDescriptor<BroadcasterConfigurationDescriptor> {

    UUID getId();

    BroadcasterType getType();

    BroadcasterCacheConfigurationDescriptor getCache();

    Map<String, Object> getAdditionalProperties();

    ConfigurationSchema getPropertiesSchema();

    void setCache(BroadcasterCacheConfigurationDescriptor cache);

    void setAdditionalProperties(Map<String, Object> additionalProperties);

    void setPropertiesSchema(ConfigurationSchema propertiesSchema);

    @Override
    default BroadcasterConfigurationDescriptor merge(BroadcasterConfigurationDescriptor other) {
        if (other == null) {
            return this;
        }

        if (!this.getCache().equals(other.getCache())) {
            this.setCache(other.getCache());
        }

        if (!this.getAdditionalProperties().equals(other.getAdditionalProperties())) {
            this.setAdditionalProperties(other.getAdditionalProperties());
        }

        if (!this.getPropertiesSchema().equals(other.getPropertiesSchema())) {
            this.setPropertiesSchema(other.getPropertiesSchema());
        }

        return this;
    }
}
