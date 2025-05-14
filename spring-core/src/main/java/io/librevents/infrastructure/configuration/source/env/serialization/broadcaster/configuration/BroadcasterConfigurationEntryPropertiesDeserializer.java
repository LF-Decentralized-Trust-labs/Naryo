package io.librevents.infrastructure.configuration.source.env.serialization.broadcaster.configuration;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.infrastructure.configuration.source.env.registry.broadcaster.BroadcasterConfigurationRegistry;
import io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterConfigurationAdditionalProperties;
import io.librevents.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterConfigurationEntryProperties;
import io.librevents.infrastructure.configuration.source.env.serialization.EnvironmentSerializer;
import org.springframework.stereotype.Component;

@Component
public final class BroadcasterConfigurationEntryPropertiesDeserializer
        extends EnvironmentSerializer<BroadcasterConfigurationEntryProperties> {

    private final BroadcasterConfigurationRegistry registry;

    public BroadcasterConfigurationEntryPropertiesDeserializer(
            BroadcasterConfigurationRegistry registry) {
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
