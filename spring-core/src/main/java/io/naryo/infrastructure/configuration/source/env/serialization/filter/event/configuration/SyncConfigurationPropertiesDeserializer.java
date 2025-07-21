package io.naryo.infrastructure.configuration.source.env.serialization.filter.event.configuration;

import java.io.IOException;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.naryo.domain.filter.event.sync.SyncStrategy;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.FilterSyncProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.block.BlockFilterSyncProperties;
import io.naryo.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class SyncConfigurationPropertiesDeserializer
        extends EnvironmentDeserializer<FilterSyncProperties> {

    @Override
    public FilterSyncProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        String typeStr = getTextOrNull(root.get("type"));
        SyncStrategy type =
                typeStr != null && !typeStr.isBlank()
                        ? SyncStrategy.valueOf(typeStr)
                        : SyncStrategy.BLOCK_BASED;

        return switch (type) {
            case BLOCK_BASED -> {
                BigInteger initialBlock =
                        safeTreeToValue(root, "initialBlock", codec, BigInteger.class);
                yield new BlockFilterSyncProperties(initialBlock);
            }
        };
    }
}
