package io.naryo.infrastructure.configuration.persistence.document.node.eth;

import java.util.UUID;

import io.naryo.domain.node.NodeType;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import io.naryo.infrastructure.configuration.persistence.document.node.NodePropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.connection.ConnectionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.InteractionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.subscription.SubscriptionPropertiesDocument;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public abstract class EthereumNodePropertiesDocument extends NodePropertiesDocument {

    private final @Getter @NotNull EthereumNodeVisibility visibility;

    protected EthereumNodePropertiesDocument(
            UUID id,
            String name,
            SubscriptionPropertiesDocument subscription,
            InteractionPropertiesDocument interaction,
            ConnectionPropertiesDocument connection,
            EthereumNodeVisibility visibility) {
        super(id, name, NodeType.ETHEREUM, subscription, interaction, connection);
        this.visibility = visibility;
    }
}
