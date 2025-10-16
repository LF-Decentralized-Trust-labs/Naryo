package io.naryo.api.node.update.model;

import java.util.UUID;

import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeName;
import io.naryo.domain.node.NodeType;
import io.naryo.domain.node.hedera.HederaNode;

public final class UpdateHederaNodeRequest extends UpdateNodeRequest {

    public UpdateHederaNodeRequest(
            String name,
            SubscriptionConfigurationRequest subscription,
            InteractionConfigurationRequest interaction,
            NodeConnectionRequest connection,
            String prevItemHash) {
        super(name, NodeType.HEDERA, subscription, interaction, connection, prevItemHash);
    }

    @Override
    public Node toDomain(UUID nodeId) {
        return new HederaNode(
                nodeId,
                new NodeName(this.name),
                this.subscription.toDomain(),
                this.interaction.toDomain(),
                this.connection.toDomain());
    }
}
