package io.naryo.infrastructure.util.serialization;

import java.util.HashMap;
import java.util.Map;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;

public abstract class ConfigurationSchemaConverter {

    public static Map<String, Object> rawObjectsToSchema(
            Map<String, Object> properties, ConfigurationSchema schema) {
        Map<String, Object> result = new HashMap<>();

        if (properties != null && !properties.isEmpty() && schema != null) {
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                schema.fields().stream()
                        .filter(field -> field.name().equals(entry.getKey()))
                        .findFirst()
                        .ifPresent(
                                field -> {
                                    Object value = convertRawValueToField(field, entry.getValue());
                                    result.put(entry.getKey(), value);
                                });
            }
        }

        return result;
    }

    private static Object convertRawValueToField(FieldDefinition field, Object rawValue) {
        Object value = TypeConverter.castToType(rawValue, field.type());
        return value != null ? value : field.defaultValue();
    }
}
