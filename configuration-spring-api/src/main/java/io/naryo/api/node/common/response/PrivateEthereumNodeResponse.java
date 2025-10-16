package io.naryo.api.node.common.response;

import java.util.UUID;

import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.ethereum.priv.PrivateEthereumNode;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;

public final class PrivateEthereumNodeResponse extends EthereumNodeResponse {

    private final String groupId;
    private final String precompiledAddress;

    public PrivateEthereumNodeResponse(
            UUID id,
            String type,
            String name,
            SubscriptionConfiguration subscriptionConfiguration,
            InteractionConfiguration interactionConfiguration,
            NodeConnection connection,
            String visibility,
            String groupId,
            String precompiledAddress) {
        super(
                id,
                type,
                name,
                subscriptionConfiguration,
                interactionConfiguration,
                connection,
                visibility);
        this.groupId = groupId;
        this.precompiledAddress = precompiledAddress;
    }

    public static PrivateEthereumNodeResponse fromDomain(PrivateEthereumNode node) {
        return new PrivateEthereumNodeResponse(
                node.getId(),
                node.getType().name(),
                node.getName().value(),
                node.getSubscriptionConfiguration(),
                node.getInteractionConfiguration(),
                node.getConnection(),
                node.getVisibility().name(),
                node.getGroupId().value(),
                node.getPrecompiledAddress().value());
    }
}
