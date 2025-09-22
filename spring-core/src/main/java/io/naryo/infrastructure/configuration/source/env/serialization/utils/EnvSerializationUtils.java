package io.naryo.infrastructure.configuration.source.env.serialization.utils;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;

public abstract class EnvSerializationUtils {

    public static Map<String, Object> jsonNodesToObjects(
            ObjectCodec codec,
            Iterator<Map.Entry<String, JsonNode>> jsonNodes,
            Set<String> knownFields,
            ConfigurationSchema schema) {
        Map<String, Object> result = new HashMap<>();

        jsonNodes.forEachRemaining(
                (entry) -> {
                    if (knownFields.contains(entry.getKey())) {
                        return;
                    }

                    schema.fields().stream()
                            .filter(field -> field.name().equals(entry.getKey()))
                            .findFirst()
                            .ifPresent(
                                    field -> {
                                        Object value =
                                                convertJsonNodeToField(
                                                        codec, field, entry.getValue());
                                        result.put(entry.getKey(), value);
                                    });
                });

        return result;
    }

    private static Object convertJsonNodeToField(
            ObjectCodec codec, FieldDefinition field, JsonNode rawValue) {
        try {
            Object value = codec.treeToValue(rawValue, field.type());
            return value != null ? value : field.defaultValue();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
