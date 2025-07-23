package io.naryo.infrastructure.configuration.persistence.document.node.eth;

import io.naryo.application.configuration.source.model.node.EthereumNodeDescriptor;
import io.naryo.infrastructure.configuration.persistence.document.node.NodePropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.connection.ConnectionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.InteractionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.subscription.SubscriptionPropertiesDocument;

public abstract class EthereumNodePropertiesDocument extends NodePropertiesDocument
        implements EthereumNodeDescriptor {

    protected EthereumNodePropertiesDocument(
            String id,
            String name,
            SubscriptionPropertiesDocument subscription,
            InteractionPropertiesDocument interaction,
            ConnectionPropertiesDocument connection) {
        super(id, name, subscription, interaction, connection);
    }
}
