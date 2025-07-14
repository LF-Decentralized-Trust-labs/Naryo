package io.naryo.infrastructure.configuration.source.env.serialization.broadcaster;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.naryo.infrastructure.configuration.source.env.model.broadcaster.BroadcastingProperties;
import io.naryo.infrastructure.configuration.source.env.model.broadcaster.configuration.BroadcasterConfigurationEntryProperties;
import io.naryo.infrastructure.configuration.source.env.model.broadcaster.target.BroadcasterEntryProperties;
import io.naryo.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class BroadcastingPropertiesDeserializer
        extends EnvironmentDeserializer<BroadcastingProperties> {

    @Override
    public BroadcastingProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        List<BroadcasterConfigurationEntryProperties> configurations =
                safeTreeToList(
                        root,
                        "configuration",
                        codec,
                        BroadcasterConfigurationEntryProperties.class);
        List<BroadcasterEntryProperties> broadcasters =
                safeTreeToList(root, "broadcasters", codec, BroadcasterEntryProperties.class);

        return new BroadcastingProperties(configurations, broadcasters);
    }
}
