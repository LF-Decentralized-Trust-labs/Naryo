package io.librevents.domain.node.hedera;

import java.util.UUID;

import io.librevents.domain.node.BaseNodeTest;
import io.librevents.domain.node.Node;
import io.librevents.domain.node.NodeName;
import io.librevents.domain.node.connection.NodeConnection;
import io.librevents.domain.node.interaction.InteractionConfiguration;
import io.librevents.domain.node.subscription.SubscriptionConfiguration;

final class HederaNodeTest extends BaseNodeTest {

    @Override
    protected Node createNode(
            UUID id,
            NodeName name,
            SubscriptionConfiguration subscriptionConfiguration,
            InteractionConfiguration interactionConfiguration,
            NodeConnection connection) {
        return new HederaNode(
                id, name, subscriptionConfiguration, interactionConfiguration, connection);
    }
}
