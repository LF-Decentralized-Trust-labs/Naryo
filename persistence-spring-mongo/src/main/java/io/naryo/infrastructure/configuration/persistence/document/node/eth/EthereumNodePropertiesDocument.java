package io.naryo.infrastructure.configuration.persistence.document.node.eth;

import java.util.UUID;

import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.domain.node.NodeType;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import io.naryo.infrastructure.configuration.persistence.document.node.NodePropertiesDocument;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public abstract class EthereumNodePropertiesDocument extends NodePropertiesDocument {

    private final @Getter @NotNull EthereumNodeVisibility visibility;

    protected EthereumNodePropertiesDocument(
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
