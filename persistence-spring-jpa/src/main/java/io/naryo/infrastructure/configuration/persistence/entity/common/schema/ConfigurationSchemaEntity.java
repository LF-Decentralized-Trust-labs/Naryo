package io.naryo.infrastructure.configuration.persistence.entity.common.schema;

import java.util.List;
import java.util.stream.Collectors;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class ConfigurationSchemaEntity {

    private @Column(name = "schema_type") String type;

    private @ElementCollection(fetch = FetchType.EAGER) @CollectionTable(
            name = "broadcasters_configuration_fields",
            joinColumns = @JoinColumn(name = "broadcaster_configuration_id")) List<
                    FieldDefinitionEntity>
            fields;

    public ConfigurationSchemaEntity(String type, List<FieldDefinitionEntity> fields) {
        this.type = type;
        this.fields = fields;
    }

    public static ConfigurationSchema fromEntity(ConfigurationSchemaEntity entity) {
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

    public static ConfigurationSchemaEntity toEntity(ConfigurationSchema schema) {
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
