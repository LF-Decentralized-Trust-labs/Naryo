package io.naryo.infrastructure.configuration.source.env.serialization.filter.event.configuration;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.SyncConfigurationAdditionalProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.SyncConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.SyncType;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.block.BlockSyncConfigurationAdditionalProperties;
import io.naryo.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class SyncConfigurationPropertiesDeserializer
        extends EnvironmentDeserializer<SyncConfigurationProperties> {

    @Override
    public SyncConfigurationProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        String typeStr = getTextOrNull(root.get("type"));
        SyncType type =
                typeStr != null && !typeStr.isBlank()
                        ? SyncType.valueOf(typeStr)
                        : SyncType.BLOCK_BASED;
        SyncConfigurationAdditionalProperties configuration =
                safeTreeToValue(
                        root,
                        "configuration",
                        codec,
                        BlockSyncConfigurationAdditionalProperties
                                .class); // Default cause BLOCK_BASED is the only one we support now

        return new SyncConfigurationProperties(type, configuration);
    }
}
