package io.naryo.infrastructure.configuration.source.env.model.event.store;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

    private final @NotNull UUID nodeId;
    private final @NotNull EventStoreType type;
    private final @NotNull EventStoreStrategy strategy;
    private Map<String, Object> additionalProperties;
    private @Nullable ConfigurationSchema propertiesSchema;

    public EventStoreConfigurationProperties(
            @NotNull UUID nodeId,
            @NotNull EventStoreType type,
            @NotNull EventStoreStrategy strategy,
            Map<String, Object> additionalProperties,
            @Nullable ConfigurationSchema propertiesSchema) {
        this.nodeId = nodeId;
        this.type = type;
        this.strategy = strategy;
        this.additionalProperties =
                additionalProperties == null ? new HashMap<>() : additionalProperties;
        this.propertiesSchema = propertiesSchema;
    }

    @Override
    public UUID getNodeId() {
        return nodeId;
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
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @Override
    public Optional<ConfigurationSchema> getPropertiesSchema() {
        return Optional.ofNullable(propertiesSchema);
    }
}
