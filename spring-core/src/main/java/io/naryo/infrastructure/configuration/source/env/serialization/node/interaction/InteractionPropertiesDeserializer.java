package io.naryo.infrastructure.configuration.source.env.serialization.node.interaction;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.naryo.domain.node.interaction.InteractionStrategy;
import io.naryo.infrastructure.configuration.source.env.model.node.interaction.InteractionConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.interaction.InteractionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.interaction.block.BlockInteractionConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class InteractionPropertiesDeserializer
        extends EnvironmentDeserializer<InteractionProperties> {

    @Override
    public InteractionProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        String strategyStr = getTextOrNull(root.get("strategy"));
        InteractionStrategy strategy =
                strategyStr != null && !strategyStr.isBlank()
                        ? InteractionStrategy.valueOf(strategyStr.toUpperCase())
                        : InteractionStrategy.BLOCK_BASED;

        InteractionConfigurationProperties configuration =
                safeTreeToValue(
                        root,
                        "configuration",
                        codec,
                        BlockInteractionConfigurationProperties
                                .class // Default cause BLOCK_BASED is the only one we support now
                        );

        return new InteractionProperties(strategy, configuration);
    }
}
