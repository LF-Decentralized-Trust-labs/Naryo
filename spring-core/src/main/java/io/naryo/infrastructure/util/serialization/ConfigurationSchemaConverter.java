package io.naryo.infrastructure.util.serialization;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ConfigurationSchemaConverter {

    private ConfigurationSchemaConverter() {}

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

    public static Map<String, Object> rawObjectsFromSchema(
            Object source, ConfigurationSchema schema) {
        Map<String, Object> rawObjects = new HashMap<>();
        schema.fields()
                .forEach(
                        field -> {
                            String fieldName = field.name();
                            try {
                                PropertyDescriptor propertyDescriptor =
                                        new PropertyDescriptor(fieldName, source.getClass());
                                Method readMethod = propertyDescriptor.getReadMethod();
                                if (readMethod != null) {
                                    Object value = readMethod.invoke(source);
                                    rawObjects.put(fieldName, value);
                                }
                            } catch (IntrospectionException
                                    | IllegalAccessException
                                    | InvocationTargetException e) {
                                log.warn("Unable to get value for field: {}", fieldName, e);
                            }
                        });
        return rawObjects;
    }

    private static Object convertRawValueToField(FieldDefinition field, Object rawValue) {
        Object value = TypeConverter.castToType(rawValue, field.type());
        return value != null ? value : field.defaultValue();
    }
}
