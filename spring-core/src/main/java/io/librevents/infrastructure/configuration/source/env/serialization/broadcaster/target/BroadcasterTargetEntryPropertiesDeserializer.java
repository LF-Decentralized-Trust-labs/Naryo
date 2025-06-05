package io.librevents.infrastructure.configuration.source.env.serialization.broadcaster.target;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.domain.broadcaster.BroadcasterTargetType;
import io.librevents.infrastructure.configuration.source.env.model.broadcaster.target.*;
import io.librevents.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class BroadcasterTargetEntryPropertiesDeserializer
        extends EnvironmentDeserializer<BroadcasterTargetEntryProperties> {

    @Override
    public BroadcasterTargetEntryProperties deserialize(
            JsonParser p, DeserializationContext context) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        String configurationId = getTextOrNull(root.get("configurationId"));
        String typeString = getTextOrNull(root.get("type"));
        BroadcasterTargetType type =
                typeString != null && !typeString.isBlank()
                        ? BroadcasterTargetType.valueOf(typeString.toUpperCase())
                        : BroadcasterTargetType.ALL;
        String destination = getTextOrNull(root.get("destination"));
        BroadcasterTargetAdditionalProperties configuration =
                safeTreeToValue(
                        root,
                        "configuration",
                        codec,
                        switch (type) {
                            case ALL -> AllBroadcasterTargetConfigurationProperties.class;
                            case TRANSACTION ->
                                    TransactionBroadcasterTargetConfigurationProperties.class;
                            case CONTRACT_EVENT ->
                                    ContractEventBroadcasterTargetConfigurationProperties.class;
                            case FILTER -> FilterBroadcasterTargetConfigurationProperties.class;
                            case BLOCK -> BlockBroadcasterTargetConfigurationProperties.class;
                        });

        return new BroadcasterTargetEntryProperties(
                getUuidOrNull(configurationId), type, destination, configuration);
    }
}
