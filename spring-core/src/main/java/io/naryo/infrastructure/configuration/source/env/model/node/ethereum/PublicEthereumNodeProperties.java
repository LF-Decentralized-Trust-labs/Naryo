package io.naryo.infrastructure.configuration.source.env.model.node.ethereum;

import java.util.UUID;

import io.naryo.application.configuration.source.model.node.PublicEthereumNodeDescriptor;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;

public final class PublicEthereumNodeProperties extends EthereumNodeProperties
        implements PublicEthereumNodeDescriptor {

    public PublicEthereumNodeProperties(
            UUID id,
            String name,
            SubscriptionDescriptor subscription,
            InteractionDescriptor interaction,
            NodeConnectionDescriptor connection) {
        super(id, name, subscription, interaction, connection, EthereumNodeVisibility.PUBLIC);
    }
}
