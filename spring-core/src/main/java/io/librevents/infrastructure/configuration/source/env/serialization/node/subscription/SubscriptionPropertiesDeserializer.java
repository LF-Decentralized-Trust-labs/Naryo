package io.librevents.infrastructure.configuration.source.env.serialization.node.subscription;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.domain.node.subscription.SubscriptionStrategy;
import io.librevents.infrastructure.configuration.source.env.model.node.subscription.SubscriptionConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.subscription.SubscriptionProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.subscription.block.BlockSubscriptionConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.serialization.EnvironmentSerializer;
import org.springframework.stereotype.Component;

@Component
public final class SubscriptionPropertiesDeserializer
        extends EnvironmentSerializer<SubscriptionProperties> {

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
