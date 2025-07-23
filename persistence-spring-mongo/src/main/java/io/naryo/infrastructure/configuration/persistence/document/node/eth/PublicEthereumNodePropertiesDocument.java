package io.naryo.infrastructure.configuration.persistence.document.node.eth;

import io.naryo.application.configuration.source.model.node.PublicEthereumNodeDescriptor;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import io.naryo.infrastructure.configuration.persistence.document.node.connection.ConnectionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.InteractionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.subscription.SubscriptionPropertiesDocument;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("public_ethereum_node")
public class PublicEthereumNodePropertiesDocument extends EthereumNodePropertiesDocument
        implements PublicEthereumNodeDescriptor {

    public PublicEthereumNodePropertiesDocument(
            String id,
            String name,
            SubscriptionPropertiesDocument subscription,
            InteractionPropertiesDocument interaction,
            ConnectionPropertiesDocument connection) {
        super(id, name, subscription, interaction, connection);
    }
}
