package io.naryo.domain.node.hedera;

import java.util.UUID;

import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeName;
import io.naryo.domain.node.NodeType;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;

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

    @Override
    public boolean supportsContractAddressInBloom() {
        return false;
    }
}
