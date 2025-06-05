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
import io.librevents.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class BlockSubscriptionConfigurationPropertiesDeserializer
        extends EnvironmentDeserializer<BlockSubscriptionConfigurationProperties> {

    @Override
    public BlockSubscriptionConfigurationProperties deserialize(
            JsonParser p, DeserializationContext context) throws IOException, JacksonException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        String initialBlockStr = getTextOrNull(root.get("initialBlock"));
        String confirmationBlocksStr = getTextOrNull(root.get("confirmationBlocks"));
        String missingTxRetryBlocksStr = getTextOrNull(root.get("missingTxRetryBlocks"));
        String eventInvalidationBlockThresholdStr =
                getTextOrNull(root.get("eventInvalidationBlockThreshold"));
        String replayBlockOffsetStr = getTextOrNull(root.get("replayBlockOffset"));
        String syncBlockLimitStr = getTextOrNull(root.get("syncBlockLimit"));
        BlockSubscriptionMethodProperties method =
                safeTreeToValue(root, "method", codec, BlockSubscriptionMethodProperties.class);

        return new BlockSubscriptionConfigurationProperties(
                method,
                parseBigInteger(initialBlockStr),
                parseBigInteger(confirmationBlocksStr),
                parseBigInteger(missingTxRetryBlocksStr),
                parseBigInteger(eventInvalidationBlockThresholdStr),
                parseBigInteger(replayBlockOffsetStr),
                parseBigInteger(syncBlockLimitStr));
    }

    private BigInteger parseBigInteger(String value) {
        return value != null && !value.isEmpty() ? new BigInteger(value) : null;
    }
}
