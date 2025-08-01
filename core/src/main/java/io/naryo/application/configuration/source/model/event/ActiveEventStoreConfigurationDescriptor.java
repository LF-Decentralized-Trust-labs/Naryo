package io.naryo.application.configuration.source.model.event;

import java.util.Map;
import java.util.Optional;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.domain.configuration.eventstore.EventStoreState;
import io.naryo.domain.configuration.eventstore.active.EventStoreStrategy;
import io.naryo.domain.configuration.eventstore.active.EventStoreType;

import static io.naryo.application.common.util.MergeUtil.*;

public interface ActiveEventStoreConfigurationDescriptor extends EventStoreConfigurationDescriptor {

    EventStoreType getType();

    EventStoreStrategy getStrategy();

    Map<String, Object> getAdditionalProperties();

    Optional<ConfigurationSchema> getPropertiesSchema();

    void setAdditionalProperties(Map<String, Object> additionalProperties);

    void setPropertiesSchema(ConfigurationSchema propertiesSchema);

    @Override
    default EventStoreState getState() {
        return EventStoreState.ACTIVE;
    }

    @Override
    default EventStoreConfigurationDescriptor merge(EventStoreConfigurationDescriptor other) {
        if (!(other
                instanceof
                ActiveEventStoreConfigurationDescriptor otherActiveEventStoreConfiguration)) {
            return this;
        }

        mergeMaps(
                this::setAdditionalProperties,
                this.getAdditionalProperties(),
                otherActiveEventStoreConfiguration.getAdditionalProperties());
        mergeOptionals(
                this::setPropertiesSchema,
                this.getPropertiesSchema(),
                otherActiveEventStoreConfiguration.getPropertiesSchema());

        return this;
    }
}
