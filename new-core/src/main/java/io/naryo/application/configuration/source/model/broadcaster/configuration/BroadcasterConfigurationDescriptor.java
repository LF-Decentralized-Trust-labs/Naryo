package io.naryo.application.configuration.source.model.broadcaster.configuration;

import static io.naryo.application.common.util.MergeUtil.mergeDescriptors;
import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.broadcaster.BroadcasterType;

public interface BroadcasterConfigurationDescriptor
        extends MergeableDescriptor<BroadcasterConfigurationDescriptor> {

    UUID getId();

    BroadcasterType getType();

    Optional <BroadcasterCacheConfigurationDescriptor> getCache();

    Optional <Map<String, Object>> getAdditionalProperties();

    Optional <ConfigurationSchema> getPropertiesSchema();

    void setCache(BroadcasterCacheConfigurationDescriptor cache);

    void setAdditionalProperties(Map<String, Object> additionalProperties);

    void setPropertiesSchema(ConfigurationSchema propertiesSchema);

    @Override
    default BroadcasterConfigurationDescriptor merge(BroadcasterConfigurationDescriptor other) {
        if (other == null) {
            return this;
        }

        mergeDescriptors(this::setCache, this.getCache(), other.getCache());
        mergeOptionals(this::setAdditionalProperties, this.getAdditionalProperties(), other.getAdditionalProperties());
        mergeOptionals(this::setPropertiesSchema, this.getPropertiesSchema(), other.getPropertiesSchema());


        return this;
    }
}
