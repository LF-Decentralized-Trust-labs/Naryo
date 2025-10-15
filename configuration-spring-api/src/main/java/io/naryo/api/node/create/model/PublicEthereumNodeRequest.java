package io.naryo.api.node.create.model;

import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;

public final class PublicEthereumNodeRequest extends EthereumNodeRequest {

    public PublicEthereumNodeRequest(
            String name,
            SubscriptionConfigurationRequest subscription,
            InteractionConfigurationRequest interaction,
            NodeConnectionRequest connection) {
        super(name, subscription, interaction, connection, EthereumNodeVisibility.PUBLIC);
    }
}
