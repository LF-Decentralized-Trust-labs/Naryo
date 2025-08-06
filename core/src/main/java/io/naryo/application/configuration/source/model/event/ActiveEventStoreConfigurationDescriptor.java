package io.naryo.application.configuration.source.model.event;

import java.util.Map;
import java.util.Optional;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.domain.configuration.store.StoreState;
import io.naryo.domain.configuration.store.active.StoreType;
import io.naryo.domain.configuration.store.active.feature.event.EventStoreStrategy;

import static io.naryo.application.common.util.MergeUtil.*;

public interface ActiveEventStoreConfigurationDescriptor extends EventStoreConfigurationDescriptor {

    StoreType getType();

    EventStoreStrategy getStrategy();

    Map<String, Object> getAdditionalProperties();

    Optional<ConfigurationSchema> getPropertiesSchema();

    void setAdditionalProperties(Map<String, Object> additionalProperties);

    void setPropertiesSchema(ConfigurationSchema propertiesSchema);

    @Override
    default StoreState getState() {
        return StoreState.ACTIVE;
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
