package io.naryo.api.node.create.model;

import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.naryo.domain.node.NodeType;

public final class HederaNodeRequest extends CreateNodeRequest {

    HederaNodeRequest(
            String name,
            SubscriptionConfigurationRequest subscription,
            InteractionConfigurationRequest interaction,
            NodeConnectionRequest connection) {
        super(name, NodeType.HEDERA, subscription, interaction, connection);
    }
}
