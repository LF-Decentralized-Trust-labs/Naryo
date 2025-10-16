package io.naryo.api.node.create.model;

import java.util.UUID;

import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeName;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import io.naryo.domain.node.ethereum.pub.PublicEthereumNode;

public final class PublicEthereumNodeRequest extends EthereumNodeRequest {

    public PublicEthereumNodeRequest(
            String name,
            SubscriptionConfigurationRequest subscription,
            InteractionConfigurationRequest interaction,
            NodeConnectionRequest connection) {
        super(name, subscription, interaction, connection, EthereumNodeVisibility.PUBLIC);
    }

    @Override
    public Node toDomain() {
        return new PublicEthereumNode(
                UUID.randomUUID(),
                new NodeName(this.name),
                this.subscription.toDomain(),
                this.interaction.toDomain(),
                this.connection.toDomain());
    }
}
