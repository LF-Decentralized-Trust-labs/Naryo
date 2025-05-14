package io.librevents.infrastructure.configuration.source.env.serialization.broadcaster.target;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.domain.broadcaster.BroadcasterTargetType;
import io.librevents.infrastructure.configuration.source.env.model.broadcaster.target.BroadcasterTargetAdditionalProperties;
import io.librevents.infrastructure.configuration.source.env.model.broadcaster.target.BroadcasterTargetEntryProperties;
import io.librevents.infrastructure.configuration.source.env.model.broadcaster.target.FilterBroadcasterTargetConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.serialization.EnvironmentSerializer;
import org.springframework.stereotype.Component;

@Component
public final class BroadcasterTargetEntryPropertiesDeserializer
        extends EnvironmentSerializer<BroadcasterTargetEntryProperties> {

    @Override
    public BroadcasterTargetEntryProperties deserialize(
            JsonParser p, DeserializationContext context) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        String configurationName = root.get("configurationName").asText();
        BroadcasterTargetType type = BroadcasterTargetType.valueOf(root.get("type").asText());
        String destination = root.get("destination").asText();
        JsonNode configurationNode = root.get("configuration");
        BroadcasterTargetAdditionalProperties configuration = null;

        if (type == BroadcasterTargetType.FILTER) {
            configuration =
                    codec.treeToValue(
                            configurationNode,
                            FilterBroadcasterTargetConfigurationProperties.class);
        }

        return new BroadcasterTargetEntryProperties(
                configurationName, type, destination, configuration);
    }
}
