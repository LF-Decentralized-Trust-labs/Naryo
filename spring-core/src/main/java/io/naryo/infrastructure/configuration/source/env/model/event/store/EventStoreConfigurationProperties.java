package io.naryo.infrastructure.configuration.source.env.model.event.store;

import java.util.Map;
import java.util.Optional;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.model.event.EventStoreConfigurationDescriptor;
import io.naryo.domain.configuration.eventstore.EventStoreStrategy;
import io.naryo.domain.configuration.eventstore.EventStoreType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;

@Setter
public abstract class EventStoreConfigurationProperties
        implements EventStoreConfigurationDescriptor {

    private @NotNull EventStoreType type;
    private @NotNull EventStoreStrategy strategy;
    private @Nullable Map<String, Object> additionalProperties;
    private @Nullable ConfigurationSchema propertiesSchema;

    public EventStoreConfigurationProperties(
            @NotNull EventStoreType type,
            @NotNull EventStoreStrategy strategy,
            @Nullable Map<String, Object> additionalProperties,
            @Nullable ConfigurationSchema propertiesSchema) {
        this.type = type;
        this.strategy = strategy;
        this.additionalProperties = additionalProperties;
        this.propertiesSchema = propertiesSchema;
    }

    @Override
    public EventStoreType getType() {
        return type;
    }

    @Override
    public EventStoreStrategy getStrategy() {
        return strategy;
    }

    @Override
    public Optional<Map<String, Object>> getAdditionalProperties() {
        return Optional.ofNullable(additionalProperties);
    }

    @Override
    public Optional<ConfigurationSchema> getPropertiesSchema() {
        return Optional.ofNullable(propertiesSchema);
    }
}
