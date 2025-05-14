package io.librevents.infrastructure.configuration.broadcaster;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.infrastructure.configuration.broadcaster.configuration.BroadcasterConfigurationEntryProperties;
import io.librevents.infrastructure.configuration.broadcaster.target.BroadcasterTargetEntryProperties;

public final class BroadcastingPropertiesDeserializer
        extends JsonDeserializer<BroadcastingProperties> {

    @Override
    public BroadcastingProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        JsonNode configurationNode = root.get("configuration");
        List<BroadcasterConfigurationEntryProperties> configurations = new java.util.ArrayList<>();

        for (JsonNode nodeEntry : configurationNode) {
            configurations.add(
                    codec.treeToValue(nodeEntry, BroadcasterConfigurationEntryProperties.class));
        }

        JsonNode broadcastersNode = root.get("broadcasters");
        List<BroadcasterTargetEntryProperties> broadcasters = new java.util.ArrayList<>();

        for (JsonNode nodeEntry : broadcastersNode) {
            broadcasters.add(codec.treeToValue(nodeEntry, BroadcasterTargetEntryProperties.class));
        }

        return new BroadcastingProperties(configurations, broadcasters);
    }
}
