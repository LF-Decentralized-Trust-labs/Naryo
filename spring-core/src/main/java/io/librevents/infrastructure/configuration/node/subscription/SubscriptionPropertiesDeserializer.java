package io.librevents.infrastructure.configuration.node.subscription;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.domain.node.subscription.SubscriptionStrategy;
import io.librevents.infrastructure.configuration.node.subscription.block.BlockSubscriptionConfigurationProperties;

public final class SubscriptionPropertiesDeserializer
        extends JsonDeserializer<SubscriptionProperties> {

    @Override
    public SubscriptionProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException, JacksonException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        SubscriptionStrategy strategy = SubscriptionStrategy.valueOf(root.get("strategy").asText());
        JsonNode configurationNode = root.get("configuration");
        SubscriptionConfigurationProperties configuration = null;

        if (strategy == SubscriptionStrategy.BLOCK_BASED) {
            configuration =
                    codec.treeToValue(
                            configurationNode, BlockSubscriptionConfigurationProperties.class);
        }

        return new SubscriptionProperties(strategy, configuration);
    }
}
