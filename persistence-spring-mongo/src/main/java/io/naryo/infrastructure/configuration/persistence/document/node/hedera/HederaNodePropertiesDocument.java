package io.naryo.infrastructure.configuration.persistence.document.node.hedera;

import java.util.UUID;

import io.naryo.application.configuration.source.model.node.HederaNodeDescriptor;
import io.naryo.domain.node.NodeType;
import io.naryo.infrastructure.configuration.persistence.document.node.NodePropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.connection.ConnectionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.InteractionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.subscription.SubscriptionPropertiesDocument;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("hedera_node")
public final class HederaNodePropertiesDocument extends NodePropertiesDocument
        implements HederaNodeDescriptor {

    public HederaNodePropertiesDocument(
            UUID id,
            String name,
            SubscriptionPropertiesDocument subscription,
            InteractionPropertiesDocument interaction,
            ConnectionPropertiesDocument connection) {
        super(id, name, NodeType.ETHEREUM, subscription, interaction, connection);
    }
}
