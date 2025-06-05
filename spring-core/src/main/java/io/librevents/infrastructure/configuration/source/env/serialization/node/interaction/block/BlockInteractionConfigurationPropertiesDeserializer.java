package io.librevents.infrastructure.configuration.source.env.serialization.node.interaction.block;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.domain.node.interaction.block.InteractionMode;
import io.librevents.infrastructure.configuration.source.env.model.node.interaction.block.BlockInteractionConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.interaction.block.BlockInteractionModeConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.interaction.block.EthereumRpcBlockInteractionModeConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.interaction.block.HederaMirrorNodeBlockInteractionModeConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class BlockInteractionConfigurationPropertiesDeserializer
        extends EnvironmentDeserializer<BlockInteractionConfigurationProperties> {

    @Override
    public BlockInteractionConfigurationProperties deserialize(
            JsonParser p, DeserializationContext context) throws IOException, JacksonException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        String modeStr = getTextOrNull(root.get("mode"));
        InteractionMode mode =
                modeStr != null && !modeStr.isBlank()
                        ? InteractionMode.valueOf(modeStr.toUpperCase())
                        : InteractionMode.ETHEREUM_RPC;
        BlockInteractionModeConfigurationProperties configuration =
                safeTreeToValue(
                        root,
                        "configuration",
                        codec,
                        switch (mode) {
                            case ETHEREUM_RPC ->
                                    EthereumRpcBlockInteractionModeConfigurationProperties.class;
                            case HEDERA_MIRROR_NODE ->
                                    HederaMirrorNodeBlockInteractionModeConfigurationProperties
                                            .class;
                        });

        return new BlockInteractionConfigurationProperties(mode, configuration);
    }
}
