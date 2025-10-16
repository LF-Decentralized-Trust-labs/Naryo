package io.naryo.api.node.common.response;

import java.util.UUID;

import io.naryo.domain.node.Node;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;

public final class HederaNodeResponse extends NodeResponse {

    public HederaNodeResponse(
            UUID id,
            String type,
            String name,
            SubscriptionConfiguration subscriptionConfiguration,
            InteractionConfiguration interactionConfiguration,
            NodeConnection connection) {
        super(id, type, name, subscriptionConfiguration, interactionConfiguration, connection);
    }

    public static HederaNodeResponse fromDomain(Node node) {
        return new HederaNodeResponse(
                node.getId(),
                node.getType().name(),
                node.getName().value(),
                node.getSubscriptionConfiguration(),
                node.getInteractionConfiguration(),
                node.getConnection());
    }
}
