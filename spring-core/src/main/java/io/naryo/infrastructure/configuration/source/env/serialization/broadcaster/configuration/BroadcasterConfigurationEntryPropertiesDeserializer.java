package io.naryo.infrastructure.configuration.source.env.serialization.broadcaster.configuration;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.naryo.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterCacheProperties;
import io.naryo.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterConfigurationAdditionalProperties;
import io.naryo.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterConfigurationEntryProperties;
import io.naryo.infrastructure.configuration.source.env.registry.broadcaster.BroadcasterConfigurationPropertiesRegistry;
import io.naryo.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
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

        String id = getTextOrNull(root.get("id"));
        String type = getTextOrNull(root.get("type"));
        BroadcasterCacheProperties cache =
                safeTreeToValue(root, "cache", codec, BroadcasterCacheProperties.class);
        BroadcasterConfigurationAdditionalProperties configuration =
                safeTreeToValue(root, "configuration", codec, registry.get(type));

        return new BroadcasterConfigurationEntryProperties(
                getUuidOrNull(id), type, cache, configuration);
    }
}
