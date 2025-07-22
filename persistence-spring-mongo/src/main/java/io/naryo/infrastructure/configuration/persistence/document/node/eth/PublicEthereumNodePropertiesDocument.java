package io.naryo.infrastructure.configuration.persistence.document.node.eth;

import java.util.UUID;

import io.naryo.application.configuration.source.model.node.PublicEthereumNodeDescriptor;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("public_ethereum_node")
public class PublicEthereumNodePropertiesDocument extends EthereumNodePropertiesDocument
        implements PublicEthereumNodeDescriptor {

    public PublicEthereumNodePropertiesDocument(
            UUID id,
            String name,
            SubscriptionDescriptor subscription,
            InteractionDescriptor interaction,
            NodeConnectionDescriptor connection) {
        super(id, name, subscription, interaction, connection, EthereumNodeVisibility.PUBLIC);
    }
}
