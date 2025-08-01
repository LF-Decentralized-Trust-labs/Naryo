package io.naryo.infrastructure.configuration.persistence.document.common;

import java.util.List;
import java.util.stream.Collectors;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Document
public class ConfigurationSchemaDocument {
    private String type;
    private List<FieldDefinitionDocument> fields;

    public static ConfigurationSchema fromDocument(ConfigurationSchemaDocument doc) {
        if (doc == null) return null;

        List<FieldDefinition> fields =
                doc.getFields().stream()
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

        return new ConfigurationSchema(doc.getType(), fields);
    }

    public static ConfigurationSchemaDocument toDocument(ConfigurationSchema schema) {
        if (schema == null) return null;

        List<FieldDefinitionDocument> fieldDocs =
                schema.fields().stream()
                        .map(
                                f ->
                                        new FieldDefinitionDocument(
                                                f.name(),
                                                f.type().getName(),
                                                f.required(),
                                                f.defaultValue()))
                        .collect(Collectors.toList());

        return new ConfigurationSchemaDocument(schema.type(), fieldDocs);
    }
}
