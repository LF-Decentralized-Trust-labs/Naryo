package io.naryo.infrastructure.configuration.source.env.serialization.node.subscription;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.naryo.domain.node.subscription.SubscriptionStrategy;
import io.naryo.infrastructure.configuration.source.env.model.node.subscription.SubscriptionConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.subscription.SubscriptionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.subscription.block.BlockSubscriptionConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class SubscriptionPropertiesDeserializer
        extends EnvironmentDeserializer<SubscriptionProperties> {

    @Override
    public SubscriptionProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        String strategyStr = getTextOrNull(root.get("strategy"));
        SubscriptionStrategy strategy =
                strategyStr != null && !strategyStr.isBlank()
                        ? SubscriptionStrategy.valueOf(strategyStr)
                        : SubscriptionStrategy.BLOCK_BASED;
        SubscriptionConfigurationProperties configuration =
                safeTreeToValue(
                        root,
                        "configuration",
                        codec,
                        BlockSubscriptionConfigurationProperties
                                .class); // Default cause BLOCK_BASED is the only one we support now
        return new SubscriptionProperties(strategy, configuration);
    }
}
