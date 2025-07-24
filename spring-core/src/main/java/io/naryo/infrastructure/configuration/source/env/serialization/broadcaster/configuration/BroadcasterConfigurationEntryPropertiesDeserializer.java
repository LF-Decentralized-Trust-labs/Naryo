package io.naryo.infrastructure.configuration.source.env.serialization.broadcaster.configuration;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.FieldDefinition;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterCacheProperties;
import io.naryo.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterConfigurationEntryProperties;
import io.naryo.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class BroadcasterConfigurationEntryPropertiesDeserializer
        extends EnvironmentDeserializer<BroadcasterConfigurationEntryProperties> {

    private static final String PREFIX_CONFIGURATION_SCHEMA = "broadcaster_";
    private final ConfigurationSchemaRegistry schemaRegistry;

    public BroadcasterConfigurationEntryPropertiesDeserializer(
            ConfigurationSchemaRegistry schemaRegistry) {
        this.schemaRegistry = schemaRegistry;
    }

    @Override
    public BroadcasterConfigurationEntryProperties deserialize(
            JsonParser p, DeserializationContext context) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        String id = getTextOrNull(root.get("id"));
        String type = getTextOrNull(root.get("type"));
        BroadcasterCacheProperties cache =
                safeTreeToValue(root, "cache", codec, BroadcasterCacheProperties.class);
        ConfigurationSchema schema = schemaRegistry.getSchema(PREFIX_CONFIGURATION_SCHEMA + type);

        Set<String> knownFields = Set.of("id", "type", "cache");

        Map<String, Object> additionalConfiguration = new HashMap<>();
        Iterator<Map.Entry<String, JsonNode>> fields = root.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            if (!knownFields.contains(entry.getKey())) {
                Optional<FieldDefinition> field =
                        schema.fields().stream()
                                .filter(f -> f.name().equals(entry.getKey()))
                                .findFirst();
                if (field.isPresent()) {
                    Object value = codec.treeToValue(entry.getValue(), field.get().type());
                    additionalConfiguration.put(
                            entry.getKey(), value != null ? value : field.get().defaultValue());
                }
            }
        }

        return new BroadcasterConfigurationEntryProperties(
                getUuidOrNull(id), () -> type, cache, additionalConfiguration, schema);
    }
}
