package io.librevents.infrastructure.configuration.broadcaster.configuration;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.infrastructure.configuration.broadcaster.BroadcasterTypeRegistry;

public final class BroadcasterConfigurationEntryPropertiesDeserializer
        extends JsonDeserializer<BroadcasterConfigurationEntryProperties> {

    private final BroadcasterTypeRegistry registry;

    public BroadcasterConfigurationEntryPropertiesDeserializer(BroadcasterTypeRegistry registry) {
        this.registry = registry;
    }

    @Override
    public BroadcasterConfigurationEntryProperties deserialize(
            JsonParser p, DeserializationContext context) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        String name = root.get("name").asText();
        String type = root.get("type").asText();
        BroadcasterConfigurationAdditionalProperties configuration =
                codec.treeToValue(root.get("configuration"), registry.get(type));

        return new BroadcasterConfigurationEntryProperties(name, type, configuration);
    }
}
