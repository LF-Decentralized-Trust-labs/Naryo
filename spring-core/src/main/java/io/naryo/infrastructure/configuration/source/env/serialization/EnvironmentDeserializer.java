package io.naryo.infrastructure.configuration.source.env.serialization;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;

public abstract class EnvironmentDeserializer<T> extends JsonDeserializer<T> {

    protected String getTextOrNull(JsonNode node) {
        return node != null && !node.isNull() ? node.asText() : null;
    }

    protected UUID getUuidOrNull(String id) {
        return id != null && !id.isBlank() ? UUID.fromString(id) : null;
    }

    protected <V> V safeTreeToValue(
            JsonNode root, String fieldName, ObjectCodec codec, Class<? extends V> clazz)
            throws IOException {
        JsonNode node = root.get(fieldName);
        V result = null;
        if (node != null && !node.isNull()) {
            result = codec.treeToValue(node, clazz);
        }
        return result != null ? result : instantiateIfHasEmptyConstructor(clazz);
    }

    protected <V> List<V> safeTreeToList(
            JsonNode root, String fieldName, ObjectCodec codec, Class<V> clazz) throws IOException {
        List<V> list = new java.util.ArrayList<>();
        JsonNode arrayNode = root.get(fieldName);
        if (arrayNode != null && !arrayNode.isNull()) {
            for (JsonNode item : arrayNode) {
                list.add(codec.treeToValue(item, clazz));
            }
        }
        return list;
    }

    protected static Map<String, Object> jsonNodesToObjects(
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

    private static <T> T instantiateIfHasEmptyConstructor(Class<T> clazz) {
        try {
            Constructor<T> ctor = clazz.getDeclaredConstructor();
            ctor.setAccessible(true);
            return ctor.newInstance();
        } catch (NoSuchMethodException e) {
            return null;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to instantiate " + clazz.getName(), e);
        }
    }
}
