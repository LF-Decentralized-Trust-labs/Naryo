package io.librevents.infrastructure.configuration.source.env.serialization.node;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.librevents.infrastructure.configuration.source.env.model.node.NodeProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.connection.ConnectionProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.interaction.InteractionProperties;
import io.librevents.infrastructure.configuration.source.env.model.node.subscription.SubscriptionProperties;
import io.librevents.infrastructure.configuration.source.env.serialization.EnvironmentSerializer;
import org.springframework.stereotype.Component;

@Component
public final class NodePropertiesDeserializer extends EnvironmentSerializer<NodeProperties> {

    @Override
    public NodeProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        String name = root.get("name").asText();
        SubscriptionProperties subscription =
                codec.treeToValue(root.get("subscription"), SubscriptionProperties.class);
        InteractionProperties interaction =
                codec.treeToValue(root.get("interaction"), InteractionProperties.class);
        ConnectionProperties connection =
                codec.treeToValue(root.get("connection"), ConnectionProperties.class);

        return new NodeProperties(name, subscription, interaction, connection);
    }
}
