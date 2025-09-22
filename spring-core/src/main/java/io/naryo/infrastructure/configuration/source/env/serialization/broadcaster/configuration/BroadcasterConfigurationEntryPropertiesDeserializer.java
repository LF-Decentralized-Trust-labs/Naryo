package io.naryo.infrastructure.configuration.source.env.serialization.broadcaster.configuration;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaRegistry;
import io.naryo.application.configuration.source.definition.registry.ConfigurationSchemaType;
import io.naryo.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterCacheProperties;
import io.naryo.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterConfigurationEntryProperties;
import io.naryo.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

import static io.naryo.infrastructure.configuration.source.env.serialization.utils.EnvSerializationUtils.jsonNodesToObjects;

@Component
public final class BroadcasterConfigurationEntryPropertiesDeserializer
        extends EnvironmentDeserializer<BroadcasterConfigurationEntryProperties> {

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

        ConfigurationSchema schema =
                schemaRegistry.getSchema(ConfigurationSchemaType.BROADCASTER, type);
        Set<String> knownFields = Set.of("id", "type", "cache");
        Map<String, Object> additionalConfiguration =
                jsonNodesToObjects(codec, root.fields(), knownFields, schema);

        return new BroadcasterConfigurationEntryProperties(
                getUuidOrNull(id), () -> type, cache, additionalConfiguration);
    }
}
