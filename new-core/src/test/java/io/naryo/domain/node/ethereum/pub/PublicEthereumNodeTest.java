package io.naryo.domain.node.ethereum.pub;

import java.util.UUID;

import io.naryo.domain.node.BaseNodeTest;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeName;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;

final class PublicEthereumNodeTest extends BaseNodeTest {

    @Override
    protected Node createNode(
            UUID id,
            NodeName name,
            SubscriptionConfiguration subscriptionConfiguration,
            InteractionConfiguration interactionConfiguration,
            NodeConnection connection) {
        return new PublicEthereumNode(
                id, name, subscriptionConfiguration, interactionConfiguration, connection);
    }
}
