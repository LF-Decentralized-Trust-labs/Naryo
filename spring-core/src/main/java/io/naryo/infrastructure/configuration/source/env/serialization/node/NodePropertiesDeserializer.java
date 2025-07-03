package io.naryo.infrastructure.configuration.source.env.serialization.node;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import io.naryo.domain.node.NodeType;
import io.naryo.infrastructure.configuration.source.env.model.node.NodeConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.NodeProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.ConnectionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.eth.EthereumNodeConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.hedera.HederaNodeConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.interaction.InteractionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.subscription.SubscriptionProperties;
import io.naryo.infrastructure.configuration.source.env.serialization.EnvironmentDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class NodePropertiesDeserializer extends EnvironmentDeserializer<NodeProperties> {

    @Override
    public NodeProperties deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode root = codec.readTree(p);

        String id = getTextOrNull(root.get("id"));
        String name = getTextOrNull(root.get("name"));
        String typeStr = getTextOrNull(root.get("type"));
        NodeType type =
                typeStr != null && !typeStr.isBlank()
                        ? NodeType.valueOf(typeStr.toUpperCase())
                        : NodeType.ETHEREUM;

        SubscriptionProperties subscription =
                safeTreeToValue(root, "subscription", codec, SubscriptionProperties.class);
        InteractionProperties interaction =
                safeTreeToValue(root, "interaction", codec, InteractionProperties.class);
        ConnectionProperties connection =
                safeTreeToValue(root, "connection", codec, ConnectionProperties.class);
        NodeConfigurationProperties configuration =
                safeTreeToValue(
                        root,
                        "configuration",
                        codec,
                        type == NodeType.ETHEREUM
                                ? EthereumNodeConfigurationProperties.class
                                : HederaNodeConfigurationProperties.class);

        return new NodeProperties(
                id != null ? UUID.fromString(id) : null,
                name,
                type,
                subscription,
                interaction,
                connection,
                configuration);
    }
}
