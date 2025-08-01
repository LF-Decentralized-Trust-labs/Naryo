package io.naryo.infrastructure.configuration.persistence.entity.broadcaster.configuration;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterCacheConfigurationDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterConfigurationDescriptor;
import io.naryo.domain.broadcaster.BroadcasterType;
import io.naryo.infrastructure.configuration.persistence.entity.broadcaster.configuration.converter.JsonMapConverter;
import io.naryo.infrastructure.configuration.persistence.entity.broadcaster.configuration.schema.ConfigurationSchemaEntity;
import io.naryo.infrastructure.configuration.persistence.entity.broadcaster.configuration.schema.FieldDefinitionEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "broadcasters_configuration")
@AllArgsConstructor
@NoArgsConstructor
public final class BroadcasterConfigurationEntity implements BroadcasterConfigurationDescriptor {

    private @Column(name = "id") @Id UUID id;

    private @Column(name = "type") @NotNull @NotBlank String type;

    private @Embedded @Valid BroadcasterCacheEntity cache;

    private @Convert(converter = JsonMapConverter.class) @Column(
            name = "additional_properties",
            columnDefinition = "TEXT") Map<String, Object> additionalProperties;

    private @Embedded ConfigurationSchemaEntity propertiesSchema;

    @Override
    public void setCache(BroadcasterCacheConfigurationDescriptor cache) {
        this.cache = new BroadcasterCacheEntity(cache.getExpirationTime());
    }

    @Override
    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    @Override
    public void setPropertiesSchema(ConfigurationSchema propertiesSchema) {
        this.propertiesSchema = toEntity(propertiesSchema);
    }

    @Override
    public UUID getId() {
        return this.id;
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
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties == null ? Map.of() : this.additionalProperties;
    }

    @Override
    public Optional<ConfigurationSchema> getPropertiesSchema() {
        return Optional.ofNullable(fromEntity(this.propertiesSchema));
    }

    private static ConfigurationSchema fromEntity(ConfigurationSchemaEntity entity) {
        if (entity == null) return null;

        List<FieldDefinition> fields =
                entity.getFields().stream()
                        .map(
                                fd -> {
                                    try {
                                        Class<?> clazz = Class.forName(fd.getTypeName());
                                        return new FieldDefinition(
                                                fd.getName(),
                                                clazz,
                                                fd.isRequired(),
                                                fd.getDefaultValue());
                                    } catch (ClassNotFoundException e) {
                                        throw new RuntimeException(
                                                "Class not found: " + fd.getTypeName(), e);
                                    }
                                })
                        .collect(Collectors.toList());

        return new ConfigurationSchema(entity.getType(), fields);
    }

    private static ConfigurationSchemaEntity toEntity(ConfigurationSchema schema) {
        if (schema == null) return null;

        List<FieldDefinitionEntity> fieldEntities =
                schema.fields().stream()
                        .map(
                                f ->
                                        new FieldDefinitionEntity(
                                                f.name(),
                                                f.type().getName(),
                                                f.required(),
                                                f.defaultValue()))
                        .collect(Collectors.toList());

        return new ConfigurationSchemaEntity(schema.type(), fieldEntities);
    }
}
