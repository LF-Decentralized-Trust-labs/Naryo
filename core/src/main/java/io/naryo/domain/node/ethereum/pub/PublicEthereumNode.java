package io.naryo.domain.node.ethereum.pub;

import java.util.UUID;

import io.naryo.domain.node.NodeName;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.ethereum.EthereumNode;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public final class PublicEthereumNode extends EthereumNode {
    public PublicEthereumNode(
            UUID id,
            NodeName name,
            SubscriptionConfiguration subscriptionConfiguration,
            InteractionConfiguration interactionConfiguration,
            NodeConnection connection) {
        super(
                id,
                name,
                subscriptionConfiguration,
                interactionConfiguration,
                connection,
                EthereumNodeVisibility.PUBLIC);
    }
}
