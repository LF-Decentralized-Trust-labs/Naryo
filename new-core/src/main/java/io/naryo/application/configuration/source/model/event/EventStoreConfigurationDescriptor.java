package io.naryo.application.configuration.source.model.event;

import java.util.Map;
import java.util.Optional;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.configuration.eventstore.EventStoreStrategy;
import io.naryo.domain.configuration.eventstore.EventStoreType;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface EventStoreConfigurationDescriptor
        extends MergeableDescriptor<EventStoreConfigurationDescriptor> {

    EventStoreType getType();

    EventStoreStrategy getStrategy();

    Optional<Map<String, Object>> getAdditionalProperties();

    Optional<ConfigurationSchema> getPropertiesSchema();

    void setAdditionalProperties(Map<String, Object> additionalProperties);

    void setPropertiesSchema(ConfigurationSchema propertiesSchema);

    @Override
    default EventStoreConfigurationDescriptor merge(EventStoreConfigurationDescriptor other) {
        if (other == null) {
            return this;
        }

        mergeOptionals(
                this::setAdditionalProperties,
                this.getAdditionalProperties(),
                other.getAdditionalProperties());
        mergeOptionals(
                this::setPropertiesSchema, this.getPropertiesSchema(), other.getPropertiesSchema());

        return this;
    }
}
