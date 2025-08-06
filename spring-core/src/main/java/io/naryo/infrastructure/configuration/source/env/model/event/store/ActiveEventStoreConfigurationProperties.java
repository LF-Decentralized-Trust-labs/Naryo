package io.naryo.infrastructure.configuration.source.env.model.event.store;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.model.event.ActiveEventStoreConfigurationDescriptor;
import io.naryo.domain.configuration.store.active.StoreType;
import io.naryo.domain.configuration.store.active.feature.event.EventStoreStrategy;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;

@Setter
public abstract class ActiveEventStoreConfigurationProperties
        extends EventStoreConfigurationProperties
        implements ActiveEventStoreConfigurationDescriptor {

    private final @NotNull StoreType type;
    private final @NotNull EventStoreStrategy strategy;
    private Map<String, Object> additionalProperties;
    private @Nullable ConfigurationSchema propertiesSchema;

    public ActiveEventStoreConfigurationProperties(
            @NotNull UUID nodeId,
            @NotNull StoreType type,
            @NotNull EventStoreStrategy strategy,
            Map<String, Object> additionalProperties,
            @Nullable ConfigurationSchema propertiesSchema) {
        super(nodeId);
        this.type = type;
        this.strategy = strategy;
        this.additionalProperties =
                additionalProperties == null ? new HashMap<>() : additionalProperties;
        this.propertiesSchema = propertiesSchema;
    }

    @Override
    public StoreType getType() {
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
