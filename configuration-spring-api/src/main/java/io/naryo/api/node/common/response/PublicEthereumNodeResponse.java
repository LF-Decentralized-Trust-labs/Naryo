package io.naryo.api.node.common.response;

import java.util.UUID;

import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.ethereum.pub.PublicEthereumNode;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;

public final class PublicEthereumNodeResponse extends EthereumNodeResponse {

    public PublicEthereumNodeResponse(
            UUID id,
            String type,
            String name,
            SubscriptionConfiguration subscriptionConfiguration,
            InteractionConfiguration interactionConfiguration,
            NodeConnection connection,
            String visibility) {
        super(
                id,
                type,
                name,
                subscriptionConfiguration,
                interactionConfiguration,
                connection,
                visibility);
    }

    public static PublicEthereumNodeResponse fromDomain(PublicEthereumNode node) {
        return new PublicEthereumNodeResponse(
                node.getId(),
                node.getType().name(),
                node.getName().value(),
                node.getSubscriptionConfiguration(),
                node.getInteractionConfiguration(),
                node.getConnection(),
                node.getVisibility().name());
    }
}
