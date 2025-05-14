package io.librevents.infrastructure.configuration.source.env.serialization.node.subscription.block.method;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.domain.node.subscription.block.method.BlockSubscriptionMethod;
import io.librevents.infrastructure.configuration.source.env.model.node.subscription.block.method.BlockSubscriptionMethodConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.subscription.block.method.BlockSubscriptionMethodProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.subscription.block.method.PollBlockSubscriptionMethodConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.subscription.block.method.PubSubBlockSubscriptionMethodConfigurationProperties;
import io.librevents.infrastructure.configuration.source.env.serialization.EnvironmentSerializer;
import org.springframework.stereotype.Component;

@Component
public final class BlockSubscriptionMethodPropertiesDeserializer
        extends EnvironmentSerializer<BlockSubscriptionMethodProperties> {

    @Override
    public BlockSubscriptionMethodProperties deserialize(
            JsonParser p, DeserializationContext context) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        BlockSubscriptionMethod method = BlockSubscriptionMethod.valueOf(root.get("type").asText());
        JsonNode configurationNode = root.get("configuration");
        BlockSubscriptionMethodConfigurationProperties configuration = null;

        switch (method) {
            case BlockSubscriptionMethod.POLL ->
                    configuration =
                            codec.treeToValue(
                                    configurationNode,
                                    PollBlockSubscriptionMethodConfigurationProperties.class);
            case BlockSubscriptionMethod.PUBSUB ->
                    configuration =
                            codec.treeToValue(
                                    configurationNode,
                                    PubSubBlockSubscriptionMethodConfigurationProperties.class);
        }

        return new BlockSubscriptionMethodProperties(method, configuration);
    }
}
