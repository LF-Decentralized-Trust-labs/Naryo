package io.naryo.infrastructure.configuration.persistence.document.broadcaster.configuration;

import com.mongodb.lang.Nullable;
import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterCacheConfigurationDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterConfigurationDescriptor;

import io.naryo.domain.broadcaster.BroadcasterType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Document(collection = "broadcasters_configuration")
@AllArgsConstructor
public final class BroadcasterConfigurationDocument implements BroadcasterConfigurationDescriptor {

    @MongoId
    private final String id;

    @NotNull
    @NotBlank
    private final String type;

    @Nullable
    @Valid
    private BroadcasterCacheConfiguration cache;

    @Nullable
    private Map<String, Object> additionalProperties;

    @Nullable
    private ConfigurationSchemaDocument propertiesSchema;

    @Override
    public UUID getId() {
        return UUID.fromString(this.id);
    }

    @Override
    public BroadcasterType getType() {
        return () -> this.type.toLowerCase();
    }

    @Override
    public Optional<BroadcasterCacheConfigurationDescriptor> getCache() {
        return Optional.ofNullable(this.cache);
    }

    @Override
    public Optional<Map<String, Object>> getAdditionalProperties() {
        return Optional.ofNullable(this.additionalProperties);
    }

    @Override
    public Optional<ConfigurationSchema> getPropertiesSchema() {
        return Optional.ofNullable(fromDocument(this.propertiesSchema));
    }

    @Override
    public void setCache(BroadcasterCacheConfigurationDescriptor cache) {
        this.cache = (BroadcasterCacheConfiguration) cache;
    }

    @Override
    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    @Override
    public void setPropertiesSchema(ConfigurationSchema propertiesSchema) {
        this.propertiesSchema = toDocument(propertiesSchema);
    }

    private static ConfigurationSchema fromDocument(ConfigurationSchemaDocument doc) {
        if (doc == null) return null;

        List<FieldDefinition> fields = doc.getFields().stream()
            .map(fd -> {
                try {
                    Class<?> clazz = Class.forName(fd.getTypeName());
                    return new FieldDefinition(fd.getName(), clazz, fd.isRequired(), fd.getDefaultValue());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Class not found: " + fd.getTypeName(), e);
                }
            })
            .collect(Collectors.toList());

        return new ConfigurationSchema(doc.getType(), fields);
    }

    private static ConfigurationSchemaDocument toDocument(ConfigurationSchema schema) {
        if (schema == null) return null;

        List<FieldDefinitionDocument> fieldDocs = schema.fields().stream()
            .map(f -> new FieldDefinitionDocument(
                f.name(),
                f.type().getName(),
                f.required(),
                f.defaultValue()
            ))
            .collect(Collectors.toList());

        return new ConfigurationSchemaDocument(schema.type(), fieldDocs);
    }

}
