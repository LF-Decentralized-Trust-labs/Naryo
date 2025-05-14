package io.librevents.infrastructure.configuration.node.interaction;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.domain.node.interaction.InteractionStrategy;
import io.librevents.infrastructure.configuration.node.interaction.block.BlockInteractionConfigurationProperties;

public final class InteractionPropertiesDeserializer
        extends JsonDeserializer<InteractionProperties> {

    @Override
    public InteractionProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        InteractionStrategy strategy = InteractionStrategy.valueOf(root.get("strategy").asText());
        JsonNode configurationNode = root.get("configuration");
        InteractionConfigurationProperties configuration = null;

        if (strategy == InteractionStrategy.BLOCK_BASED) {
            configuration =
                    codec.treeToValue(
                            configurationNode, BlockInteractionConfigurationProperties.class);
        }

        return new InteractionProperties(strategy, configuration);
    }
}
