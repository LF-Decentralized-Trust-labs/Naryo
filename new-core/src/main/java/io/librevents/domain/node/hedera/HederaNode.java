package io.librevents.domain.node.hedera;

import java.util.UUID;

import io.librevents.domain.node.Node;
import io.librevents.domain.node.NodeName;
import io.librevents.domain.node.NodeType;
import io.librevents.domain.node.connection.NodeConnection;
import io.librevents.domain.node.interaction.InteractionConfiguration;
import io.librevents.domain.node.subscription.SubscriptionConfiguration;

public final class HederaNode extends Node {
    public HederaNode(
            UUID id,
            NodeName name,
            SubscriptionConfiguration subscriptionConfiguration,
            InteractionConfiguration interactionConfiguration,
            NodeConnection connection) {
        super(
                id,
                name,
                NodeType.HEDERA,
                subscriptionConfiguration,
                interactionConfiguration,
                connection);
    }

}
