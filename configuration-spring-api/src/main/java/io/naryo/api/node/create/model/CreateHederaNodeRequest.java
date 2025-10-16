package io.naryo.api.node.create.model;

import java.util.UUID;

import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeName;
import io.naryo.domain.node.NodeType;
import io.naryo.domain.node.hedera.HederaNode;

public final class CreateHederaNodeRequest extends CreateNodeRequest {

    CreateHederaNodeRequest(
            String name,
            SubscriptionConfigurationRequest subscription,
            InteractionConfigurationRequest interaction,
            NodeConnectionRequest connection) {
        super(name, NodeType.HEDERA, subscription, interaction, connection);
    }

    @Override
    public Node toDomain() {
        return new HederaNode(
                UUID.randomUUID(),
                new NodeName(this.name),
                this.subscription.toDomain(),
                this.interaction.toDomain(),
                this.connection.toDomain());
    }
}
