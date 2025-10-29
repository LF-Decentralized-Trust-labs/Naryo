package io.naryo.api.node.getAll.model;

import java.util.UUID;

import io.naryo.api.node.getAll.model.connection.NodeConnectionResponse;
import io.naryo.api.node.getAll.model.interaction.InteractionConfigurationResponse;
import io.naryo.api.node.getAll.model.subscription.SubscriptionConfigurationResponse;
import io.naryo.domain.node.ethereum.EthereumNode;
import io.naryo.domain.node.ethereum.priv.PrivateEthereumNode;
import io.naryo.domain.node.ethereum.pub.PublicEthereumNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Ethereum node")
@Getter
public abstract class EthereumNodeResponse extends NodeResponse {

    protected EthereumNodeResponse(
            UUID id,
            String name,
            SubscriptionConfigurationResponse subscriptionConfiguration,
            InteractionConfigurationResponse interactionConfiguration,
            NodeConnectionResponse connection,
            String currentItemHash) {
        super(
                id,
                name,
                subscriptionConfiguration,
                interactionConfiguration,
                connection,
                currentItemHash);
    }

    public static EthereumNodeResponse fromDomain(
            EthereumNode ethereumNode, String currentItemHash) {
        return switch (ethereumNode) {
            case PrivateEthereumNode privateEthereumNode ->
                    PrivateEthereumNodeResponse.fromDomain(privateEthereumNode, currentItemHash);
            case PublicEthereumNode privateEthereumNode ->
                    PublicEthereumNodeResponse.fromDomain(privateEthereumNode, currentItemHash);
            default -> throw new IllegalStateException("Unexpected value: " + ethereumNode);
        };
    }
}
