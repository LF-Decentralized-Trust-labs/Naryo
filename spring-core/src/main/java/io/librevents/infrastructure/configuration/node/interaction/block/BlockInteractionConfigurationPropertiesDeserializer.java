package io.librevents.infrastructure.configuration.node.interaction.block;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.domain.node.interaction.block.InteractionMode;

public final class BlockInteractionConfigurationPropertiesDeserializer
        extends JsonDeserializer<BlockInteractionConfigurationProperties> {

    @Override
    public BlockInteractionConfigurationProperties deserialize(
            JsonParser p, DeserializationContext context) throws IOException, JacksonException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        InteractionMode mode = InteractionMode.valueOf(root.get("mode").asText());
        JsonNode configurationNode = root.get("configuration");
        BlockInteractionModeConfigurationProperties configuration = null;

        switch (mode) {
            case InteractionMode.ETHEREUM_RPC ->
                    configuration =
                            codec.treeToValue(
                                    configurationNode,
                                    EthRpcBlockInteractionModeConfigurationProperties.class);
            case InteractionMode.HEDERA_MIRROR_NODE ->
                    configuration =
                            codec.treeToValue(
                                    configurationNode,
                                    HederaMirrorNodeBlockInteractionModeConfigurationProperties
                                            .class);
        }

        return new BlockInteractionConfigurationProperties(mode, configuration);
    }
}
