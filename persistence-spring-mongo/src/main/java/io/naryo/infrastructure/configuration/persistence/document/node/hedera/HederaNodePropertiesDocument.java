package io.naryo.infrastructure.configuration.persistence.document.node.hedera;

import java.util.UUID;

import io.naryo.application.configuration.source.model.node.HederaNodeDescriptor;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.domain.node.NodeType;
import io.naryo.infrastructure.configuration.persistence.document.node.NodePropertiesDocument;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("hedera_node")
public final class HederaNodePropertiesDocument extends NodePropertiesDocument
        implements HederaNodeDescriptor {

    public HederaNodePropertiesDocument(
            UUID id,
            String name,
            SubscriptionDescriptor subscription,
            InteractionDescriptor interaction,
            NodeConnectionDescriptor connection) {
        super(id, name, NodeType.ETHEREUM, subscription, interaction, connection);
    }
}
