package io.librevents.infrastructure.configuration.source.env.serialization.node.subscription.block;

import java.io.IOException;
import java.math.BigInteger;

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
        BigInteger initialBlock = BigInteger.valueOf(root.get("initialBlock").asInt());
        BigInteger confirmationBlocks = BigInteger.valueOf(root.get("confirmationBlocks").asInt());
        BigInteger missingTxRetryBlocks =
                BigInteger.valueOf(root.get("missingTxRetryBlocks").asInt());
        BigInteger eventInvalidationBlockThreshold =
                BigInteger.valueOf(root.get("eventInvalidationBlockThreshold").asInt());
        BigInteger replayBlockOffset = BigInteger.valueOf(root.get("replayBlockOffset").asInt());
        BigInteger syncBlockLimit = BigInteger.valueOf(root.get("syncBlockLimit").asInt());

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
