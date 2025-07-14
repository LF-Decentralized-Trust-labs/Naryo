package io.naryo.infrastructure.configuration.source.env.serialization.broadcaster.target;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.naryo.application.configuration.source.model.broadcaster.target.BroadcasterTargetDescriptor;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import io.naryo.infrastructure.configuration.source.env.model.broadcaster.target.*;
import io.naryo.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class BroadcasterTargetEntryPropertiesDeserializer
        extends EnvironmentDeserializer<BroadcasterEntryProperties> {

    @Override
    public BroadcasterEntryProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        String id = getTextOrNull(root.get("id"));
        String configurationId = getTextOrNull(root.get("configurationId"));
        JsonNode targetNode = root.get("target");
        String typeString = getTextOrNull(targetNode != null ? targetNode.get("type") : null);
        BroadcasterTargetType type =
                typeString != null && !typeString.isBlank()
                        ? BroadcasterTargetType.valueOf(typeString.toUpperCase())
                        : BroadcasterTargetType.ALL;
        BroadcasterTargetDescriptor target =
                safeTreeToValue(
                        root,
                        "target",
                        codec,
                        switch (type) {
                            case ALL -> AllBroadcasterTargetProperties.class;
                            case TRANSACTION ->
                                    TransactionBroadcasterTargetConfigurationProperties.class;
                            case CONTRACT_EVENT -> ContractEventBroadcasterTargetProperties.class;
                            case FILTER -> FilterBroadcasterTargetProperties.class;
                            case BLOCK -> BlockBroadcasterTargetProperties.class;
                        });

        return new BroadcasterEntryProperties(
                getUuidOrNull(id), getUuidOrNull(configurationId), target);
    }
}
