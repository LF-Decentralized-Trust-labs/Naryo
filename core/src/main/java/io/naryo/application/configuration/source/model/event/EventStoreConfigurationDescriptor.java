package io.naryo.application.configuration.source.model.event;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.configuration.eventstore.EventStoreStrategy;
import io.naryo.domain.configuration.eventstore.EventStoreType;

import static io.naryo.application.common.util.MergeUtil.*;

public interface EventStoreConfigurationDescriptor
        extends MergeableDescriptor<EventStoreConfigurationDescriptor> {

    UUID getNodeId();

    EventStoreType getType();

    EventStoreStrategy getStrategy();

    Map<String, Object> getAdditionalProperties();

    Optional<ConfigurationSchema> getPropertiesSchema();

    void setAdditionalProperties(Map<String, Object> additionalProperties);

    void setPropertiesSchema(ConfigurationSchema propertiesSchema);

    @Override
    default EventStoreConfigurationDescriptor merge(EventStoreConfigurationDescriptor other) {
        if (other == null) {
            return this;
        }

        mergeMaps(
                this::setAdditionalProperties,
                this.getAdditionalProperties(),
                other.getAdditionalProperties());
        mergeOptionals(
                this::setPropertiesSchema, this.getPropertiesSchema(), other.getPropertiesSchema());

        return this;
    }
}
