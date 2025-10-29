package io.naryo.api.node.getAll.model;

import java.util.UUID;

import io.naryo.api.node.getAll.model.connection.NodeConnectionResponse;
import io.naryo.api.node.getAll.model.interaction.InteractionConfigurationResponse;
import io.naryo.api.node.getAll.model.subscription.SubscriptionConfigurationResponse;
import io.naryo.domain.node.ethereum.priv.PrivateEthereumNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Private Ethereum node")
@Getter
public final class PrivateEthereumNodeResponse extends EthereumNodeResponse {

    private final String groupId;
    private final String precompiledAddress;

    private PrivateEthereumNodeResponse(
            UUID id,
            String name,
            SubscriptionConfigurationResponse subscriptionConfiguration,
            InteractionConfigurationResponse interactionConfiguration,
            NodeConnectionResponse connection,
            String currentItemHash,
            String groupId,
            String precompiledAddress) {
        super(
                id,
                name,
                subscriptionConfiguration,
                interactionConfiguration,
                connection,
                currentItemHash);
        this.groupId = groupId;
        this.precompiledAddress = precompiledAddress;
    }

    public static PrivateEthereumNodeResponse fromDomain(
            PrivateEthereumNode node, String currentItemHash) {
        return new PrivateEthereumNodeResponse(
                node.getId(),
                node.getName().value(),
                SubscriptionConfigurationResponse.fromDomain(node.getSubscriptionConfiguration()),
                InteractionConfigurationResponse.fromDomain(node.getInteractionConfiguration()),
                NodeConnectionResponse.fromDomain(node.getConnection()),
                currentItemHash,
                node.getGroupId().value(),
                node.getPrecompiledAddress().value());
    }
}
