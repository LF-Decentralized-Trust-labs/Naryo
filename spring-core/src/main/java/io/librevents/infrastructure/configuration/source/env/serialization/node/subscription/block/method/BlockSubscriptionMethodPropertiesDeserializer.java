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
import io.librevents.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class BlockSubscriptionMethodPropertiesDeserializer
        extends EnvironmentDeserializer<BlockSubscriptionMethodProperties> {

    @Override
    public BlockSubscriptionMethodProperties deserialize(
            JsonParser p, DeserializationContext context) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        String methodStr = getTextOrNull(root.get("type"));
        BlockSubscriptionMethod method =
                methodStr != null && !methodStr.isBlank()
                        ? BlockSubscriptionMethod.valueOf(methodStr.toUpperCase())
                        : BlockSubscriptionMethod.POLL;
        BlockSubscriptionMethodConfigurationProperties configuration =
                safeTreeToValue(
                        root,
                        "configuration",
                        codec,
                        switch (method) {
                            case BlockSubscriptionMethod.POLL ->
                                    PollBlockSubscriptionMethodConfigurationProperties.class;
                            case BlockSubscriptionMethod.PUBSUB ->
                                    PubSubBlockSubscriptionMethodConfigurationProperties.class;
                        });

        return new BlockSubscriptionMethodProperties(method, configuration);
    }
}
