package io.naryo.application.configuration.source.model.broadcaster.configuration;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.broadcaster.BroadcasterType;

import static io.naryo.application.common.util.MergeUtil.mergeDescriptors;
import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface BroadcasterConfigurationDescriptor
        extends MergeableDescriptor<BroadcasterConfigurationDescriptor> {

    UUID getId();

    BroadcasterType getType();

    <T extends BroadcasterCacheConfigurationDescriptor> Optional<T> getCache();

    Optional<Map<String, Object>> getAdditionalProperties();

    <T extends ConfigurationSchema> Optional<T> getPropertiesSchema();

    void setCache(BroadcasterCacheConfigurationDescriptor cache);

    void setAdditionalProperties(Map<String, Object> additionalProperties);

    void setPropertiesSchema(ConfigurationSchema propertiesSchema);

    @Override
    default BroadcasterConfigurationDescriptor merge(BroadcasterConfigurationDescriptor other) {
        mergeDescriptors(this::setCache, this.getCache(), other.getCache());
        mergeOptionals(
                this::setAdditionalProperties,
                this.getAdditionalProperties(),
                other.getAdditionalProperties());
        mergeOptionals(
                this::setPropertiesSchema, this.getPropertiesSchema(), other.getPropertiesSchema());

        return this;
    }
}
