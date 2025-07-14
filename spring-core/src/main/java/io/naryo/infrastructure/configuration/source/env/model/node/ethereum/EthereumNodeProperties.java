package io.naryo.infrastructure.configuration.source.env.model.node.ethereum;

import java.util.UUID;

import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.domain.node.NodeType;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import io.naryo.infrastructure.configuration.source.env.model.node.NodeProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public abstract class EthereumNodeProperties extends NodeProperties {

    private final @Getter @NotNull EthereumNodeVisibility visibility;

    protected EthereumNodeProperties(
            UUID id,
            String name,
            SubscriptionDescriptor subscription,
            InteractionDescriptor interaction,
            NodeConnectionDescriptor connection,
            EthereumNodeVisibility visibility) {
        super(id, name, NodeType.ETHEREUM, subscription, interaction, connection);
        this.visibility = visibility;
    }
}
