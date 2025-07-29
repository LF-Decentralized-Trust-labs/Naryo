package io.naryo.infrastructure.configuration.persistence.document.event.store;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.model.event.EventStoreConfigurationDescriptor;
import io.naryo.domain.configuration.eventstore.EventStoreStrategy;
import io.naryo.domain.configuration.eventstore.EventStoreType;
import io.naryo.infrastructure.configuration.persistence.document.common.ConfigurationSchemaDocument;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Document(collection = "event_stores")
@Setter
public abstract class EventStoreConfigurationPropertiesDocument implements EventStoreConfigurationDescriptor {
    private @MongoId String nodeId;
    private @NotNull EventStoreType type;
    private @NotNull EventStoreStrategy strategy;
    private Map<String, Object> additionalProperties;
    private @Nullable ConfigurationSchemaDocument propertiesSchema;

    public EventStoreConfigurationPropertiesDocument(String nodeId, EventStoreType type, EventStoreStrategy strategy, @Nullable Map<String, Object> additionalProperties,
                                                     @Nullable ConfigurationSchemaDocument propertiesSchema) {
        this.nodeId = nodeId;
        this.type = type;
        this.strategy = strategy;
        this.additionalProperties = additionalProperties;
        this.propertiesSchema = propertiesSchema;
    }

    @Override
    public UUID getNodeId() {
        return UUID.fromString(nodeId);
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
    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    @Override
    public Optional<ConfigurationSchema> getPropertiesSchema() {
        return Optional.ofNullable(ConfigurationSchemaDocument.fromDocument(propertiesSchema));
    }

    @Override
    public void setPropertiesSchema(ConfigurationSchema propertiesSchema) {
        this.propertiesSchema = ConfigurationSchemaDocument.toDocument(propertiesSchema);
    }
}
