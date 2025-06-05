package io.librevents.infrastructure.configuration.source.env.serialization;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

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

    public static <T> T instantiateIfHasEmptyConstructor(Class<T> clazz) {
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
