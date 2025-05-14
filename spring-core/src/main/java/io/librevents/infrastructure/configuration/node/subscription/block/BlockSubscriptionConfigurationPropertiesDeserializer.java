package io.librevents.infrastructure.configuration.node.subscription.block;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.infrastructure.configuration.node.subscription.block.method.BlockSubscriptionMethodProperties;

public final class BlockSubscriptionConfigurationPropertiesDeserializer
        extends JsonDeserializer<BlockSubscriptionConfigurationProperties> {

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
