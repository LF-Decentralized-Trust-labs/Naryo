package io.librevents.infrastructure.configuration.source.env.serialization.broadcaster.configuration;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterCacheProperties;
import io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterConfigurationAdditionalProperties;
import io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterConfigurationEntryProperties;
import io.librevents.infrastructure.configuration.source.env.registry.broadcaster.BroadcasterConfigurationPropertiesRegistry;
import io.librevents.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class BroadcasterConfigurationEntryPropertiesDeserializer
        extends EnvironmentDeserializer<BroadcasterConfigurationEntryProperties> {

    private final BroadcasterConfigurationPropertiesRegistry registry;

    public BroadcasterConfigurationEntryPropertiesDeserializer(
            BroadcasterConfigurationPropertiesRegistry registry) {
        this.registry = registry;
    }

    @Override
    public BroadcasterConfigurationEntryProperties deserialize(
            JsonParser p, DeserializationContext context) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        String id = root.get("id").asText();
        String type = root.get("type").asText();
        BroadcasterCacheProperties cache =
                codec.treeToValue(root.get("cache"), BroadcasterCacheProperties.class);
        BroadcasterConfigurationAdditionalProperties configuration =
                codec.treeToValue(root.get("configuration"), registry.get(type));

        return new BroadcasterConfigurationEntryProperties(
                UUID.fromString(id), type, cache, configuration);
    }
}
