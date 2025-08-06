package io.naryo.infrastructure.configuration.persistence.document.event.store;

import java.util.Map;
import java.util.Optional;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.model.event.ActiveEventStoreConfigurationDescriptor;
import io.naryo.domain.configuration.store.StoreState;
import io.naryo.domain.configuration.store.active.StoreType;
import io.naryo.domain.configuration.store.active.feature.event.EventStoreStrategy;
import io.naryo.infrastructure.configuration.persistence.document.common.ConfigurationSchemaDocument;
import jakarta.annotation.Nullable;

public class ActiveEventStoreConfigurationPropertiesDocument
        extends EventStoreConfigurationPropertiesDocument
        implements ActiveEventStoreConfigurationDescriptor {

    private final StoreType type;
    private final EventStoreStrategy strategy;
    private Map<String, Object> additionalProperties;
    private @Nullable ConfigurationSchemaDocument propertiesSchema;

    public ActiveEventStoreConfigurationPropertiesDocument(
            String nodeId,
            StoreType type,
            EventStoreStrategy strategy,
            Map<String, Object> additionalProperties,
            @Nullable ConfigurationSchemaDocument propertiesSchema) {
        super(nodeId, StoreState.ACTIVE);
        this.type = type;
        this.strategy = strategy;
        this.additionalProperties = additionalProperties;
        this.propertiesSchema = propertiesSchema;
    }

    @Override
    public StoreType getType() {
        return this.type;
    }

    @Override
    public EventStoreStrategy getStrategy() {
        return this.strategy;
    }

    @Override
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties == null ? Map.of() : this.additionalProperties;
    }

    @Override
    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    @Override
    public Optional<ConfigurationSchema> getPropertiesSchema() {
        return Optional.ofNullable(ConfigurationSchemaDocument.fromDocument(this.propertiesSchema));
    }

    @Override
    public void setPropertiesSchema(ConfigurationSchema propertiesSchema) {
        this.propertiesSchema = ConfigurationSchemaDocument.toDocument(propertiesSchema);
    }
}
