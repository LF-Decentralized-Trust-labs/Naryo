package io.librevents.domain.node.ethereum.pub;

import java.util.UUID;

import io.librevents.domain.node.NodeName;
import io.librevents.domain.node.connection.NodeConnection;
import io.librevents.domain.node.ethereum.EthereumNode;
import io.librevents.domain.node.ethereum.EthereumNodeVisibility;
import io.librevents.domain.node.interaction.InteractionConfiguration;
import io.librevents.domain.node.subscription.SubscriptionConfiguration;

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
