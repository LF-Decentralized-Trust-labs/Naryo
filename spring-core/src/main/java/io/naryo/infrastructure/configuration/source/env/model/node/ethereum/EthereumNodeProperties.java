package io.naryo.infrastructure.configuration.source.env.model.node.ethereum;

import java.util.UUID;

import io.naryo.application.configuration.source.model.node.EthereumNodeDescriptor;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.infrastructure.configuration.source.env.model.node.NodeProperties;

public abstract class EthereumNodeProperties extends NodeProperties
        implements EthereumNodeDescriptor {

    protected EthereumNodeProperties(
            UUID id,
            String name,
            SubscriptionDescriptor subscription,
            InteractionDescriptor interaction,
            NodeConnectionDescriptor connection) {
        super(id, name, subscription, interaction, connection);
    }
}
