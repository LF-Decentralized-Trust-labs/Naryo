package io.naryo.api.node.update.model;

import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.naryo.domain.node.NodeType;

public final class UpdateHederaNodeRequest extends UpdateNodeRequest {

    UpdateHederaNodeRequest(
            String name,
            SubscriptionConfigurationRequest subscription,
            InteractionConfigurationRequest interaction,
            NodeConnectionRequest connection,
            String prevItemHash) {
        super(name, NodeType.HEDERA, subscription, interaction, connection, prevItemHash);
    }
}
