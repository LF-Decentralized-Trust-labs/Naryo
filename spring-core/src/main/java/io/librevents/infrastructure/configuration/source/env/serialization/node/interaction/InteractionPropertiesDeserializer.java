package io.librevents.infrastructure.configuration.source.env.serialization.node.interaction;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.domain.node.interaction.InteractionStrategy;
import io.librevents.infrastructure.configuration.source.env.model.node.interaction.InteractionConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.interaction.InteractionProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.interaction.block.BlockInteractionConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.serialization.EnvironmentSerializer;
import org.springframework.stereotype.Component;

@Component
public final class InteractionPropertiesDeserializer
        extends EnvironmentSerializer<InteractionProperties> {

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
