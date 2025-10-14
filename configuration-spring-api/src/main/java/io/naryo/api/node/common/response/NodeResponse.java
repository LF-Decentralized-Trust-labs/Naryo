package io.naryo.api.node.common.response;

import java.util.UUID;

import io.naryo.api.node.common.response.connection.NodeConnectionResponse;
import io.naryo.api.node.common.response.interaction.InteractionConfigurationResponse;
import io.naryo.api.node.common.response.subscription.SubscriptionConfigurationResponse;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.ethereum.EthereumNode;
import io.naryo.domain.node.ethereum.priv.PrivateEthereumNode;

public abstract class NodeResponse {

    private final UUID id;
    private final String type;
    private final String name;
    private final SubscriptionConfigurationResponse subscriptionConfiguration;
    private final InteractionConfigurationResponse interactionConfiguration;
    private final NodeConnectionResponse nodeConnection;

    public NodeResponse(
            UUID id,
            String type,
            String name,
            SubscriptionConfigurationResponse subscriptionConfiguration,
            InteractionConfigurationResponse interactionConfiguration,
            NodeConnectionResponse nodeConnection) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.subscriptionConfiguration = subscriptionConfiguration;
        this.interactionConfiguration = interactionConfiguration;
        this.nodeConnection = nodeConnection;
    }

    public static NodeResponse fromDomain(Node node) {
        return switch (node.getType()) {
            case ETHEREUM -> {
                var ethereumNode = (EthereumNode) node;
                yield switch (ethereumNode.getVisibility()) {
                    case PUBLIC ->
                            new PublicEthereumNodeResponse(
                                    node.getId(),
                                    node.getType().name(),
                                    node.getName().value(),
                                    SubscriptionConfigurationResponse.fromDomain(
                                            node.getSubscriptionConfiguration()),
                                    InteractionConfigurationResponse.fromDomain(
                                            node.getInteractionConfiguration()),
                                    NodeConnectionResponse.fromDomain(node.getConnection()),
                                    ethereumNode.getVisibility().name());
                    case PRIVATE -> {
                        var privNode = (PrivateEthereumNode) ethereumNode;
                        yield new PrivateEthereumNodeResponse(
                                node.getId(),
                                node.getType().name(),
                                node.getName().value(),
                                SubscriptionConfigurationResponse.fromDomain(
                                        node.getSubscriptionConfiguration()),
                                InteractionConfigurationResponse.fromDomain(
                                        node.getInteractionConfiguration()),
                                NodeConnectionResponse.fromDomain(node.getConnection()),
                                ethereumNode.getVisibility().name(),
                                privNode.getGroupId().value(),
                                privNode.getPrecompiledAddress().value());
                    }
                };
            }
            case HEDERA ->
                    new HederaNodeResponse(
                            node.getId(),
                            node.getType().name(),
                            node.getName().value(),
                            SubscriptionConfigurationResponse.fromDomain(
                                    node.getSubscriptionConfiguration()),
                            InteractionConfigurationResponse.fromDomain(
                                    node.getInteractionConfiguration()),
                            NodeConnectionResponse.fromDomain(node.getConnection()));
        };
    }
}
