package io.naryo.api.node.common.response;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.ethereum.EthereumNode;
import io.naryo.domain.node.ethereum.priv.PrivateEthereumNode;
import io.naryo.domain.node.ethereum.pub.PublicEthereumNode;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = EthereumNodeResponse.class, name = "ETHEREUM"),
    @JsonSubTypes.Type(value = HederaNodeResponse.class, name = "HEDERA"),
})
public abstract class NodeResponse {

    protected final UUID id;
    protected final String name;
    protected final String type;
    protected final SubscriptionConfiguration subscription;
    protected final InteractionConfiguration interaction;
    protected final NodeConnection connection;
    protected String currentItemHash;

    protected NodeResponse(
            UUID id,
            String name,
            String type,
            SubscriptionConfiguration subscription,
            InteractionConfiguration interaction,
            NodeConnection connection,
            String currentItemHash) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.subscription = subscription;
        this.interaction = interaction;
        this.connection = connection;
        this.currentItemHash = currentItemHash;
    }

    public static NodeResponse map(Node node, Map<UUID, String> fingerprints) {
        String hash = fingerprints.get(node.getId());
        return switch (node.getType()) {
            case ETHEREUM -> {
                var ethereumNode = (EthereumNode) node;
                yield switch (ethereumNode.getVisibility()) {
                    case PUBLIC ->
                            PublicEthereumNodeResponse.map((PublicEthereumNode) ethereumNode, hash);
                    case PRIVATE ->
                            PrivateEthereumNodeResponse.map(
                                    (PrivateEthereumNode) ethereumNode, hash);
                };
            }
            case HEDERA -> HederaNodeResponse.map(node, hash);
        };
    }
}
