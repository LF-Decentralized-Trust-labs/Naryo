package io.librevents.infrastructure.configuration.source.env.serialization.filter.event.configuration;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.infrastructure.configuration.source.env.model.filter.event.sync.SyncConfigurationAdditionalProperties;
import io.librevents.infrastructure.configuration.source.env.model.filter.event.sync.SyncConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.model.filter.event.sync.SyncType;
import io.librevents.infrastructure.configuration.source.env.model.filter.event.sync.block.BlockSyncConfigurationAdditionalProperties;
import io.librevents.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class SyncConfigurationPropertiesDeserializer
        extends EnvironmentDeserializer<SyncConfigurationProperties> {

    @Override
    public SyncConfigurationProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        SyncType type = SyncType.valueOf(root.get("type").asText());
        JsonNode configurationNode = root.get("configuration");
        SyncConfigurationAdditionalProperties configuration =
                switch (type) {
                    case BLOCK_BASED ->
                            codec.treeToValue(
                                    root.get("configuration"),
                                    BlockSyncConfigurationAdditionalProperties.class);
                };

        return new SyncConfigurationProperties(type, configuration);
    }
}
