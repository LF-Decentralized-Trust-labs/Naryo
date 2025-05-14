package io.librevents.infrastructure.configuration.source.env.serialization.node.subscription.block;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.infrastructure.configuration.source.env.model.node.subscription.block.BlockSubscriptionConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.subscription.block.method.BlockSubscriptionMethodProperties;
import io.librevents.infrastructure.configuration.source.env.serialization.EnvironmentSerializer;
import org.springframework.stereotype.Component;

@Component
public final class BlockSubscriptionConfigurationPropertiesDeserializer
        extends EnvironmentSerializer<BlockSubscriptionConfigurationProperties> {

    @Override
    public BlockSubscriptionConfigurationProperties deserialize(
            JsonParser p, DeserializationContext context) throws IOException, JacksonException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        BlockSubscriptionMethodProperties method =
                codec.treeToValue(root.get("method"), BlockSubscriptionMethodProperties.class);
        Integer initialBlock = root.get("initialBlock").asInt();
        Integer confirmationBlocks = root.get("confirmationBlocks").asInt();
        Integer missingTxRetryBlocks = root.get("missingTxRetryBlocks").asInt();
        Integer eventInvalidationBlockThreshold =
                root.get("eventInvalidationBlockThreshold").asInt();
        Integer replayBlockOffset = root.get("replayBlockOffset").asInt();
        Integer syncBlockLimit = root.get("syncBlockLimit").asInt();

        return new BlockSubscriptionConfigurationProperties(
                method,
                initialBlock,
                confirmationBlocks,
                missingTxRetryBlocks,
                eventInvalidationBlockThreshold,
                replayBlockOffset,
                syncBlockLimit);
    }
}
