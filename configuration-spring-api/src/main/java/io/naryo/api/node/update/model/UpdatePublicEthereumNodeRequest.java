package io.naryo.api.node.update.model;

import java.util.UUID;

import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeName;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import io.naryo.domain.node.ethereum.pub.PublicEthereumNode;

public final class UpdatePublicEthereumNodeRequest extends UpdateEthereumNodeRequest {

    public UpdatePublicEthereumNodeRequest(
            String name,
            SubscriptionConfigurationRequest subscription,
            InteractionConfigurationRequest interaction,
            NodeConnectionRequest connection,
            String prevItemHash) {
        super(
                name,
                subscription,
                interaction,
                connection,
                EthereumNodeVisibility.PUBLIC,
                prevItemHash);
    }

    @Override
    public Node toDomain(UUID nodeId) {
        return new PublicEthereumNode(
                nodeId,
                new NodeName(this.name),
                this.subscription.toDomain(),
                this.interaction.toDomain(),
                this.connection.toDomain());
    }
}
