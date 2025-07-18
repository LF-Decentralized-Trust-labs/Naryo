package io.naryo.infrastructure.configuration.persistence.document.node;

import java.util.UUID;

import io.naryo.application.configuration.source.model.node.NodeDescriptor;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.domain.node.NodeType;
import io.naryo.infrastructure.configuration.persistence.document.node.connection.NodeConnectionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.InteractionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.subscription.SubscriptionPropertiesDocument;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "nodes")
@Getter
public abstract class NodePropertiesDocument implements NodeDescriptor {

    private @MongoId UUID id;
    private @NotNull NodeType type;
    private @Setter @NotBlank String name;
    private SubscriptionPropertiesDocument subscription;
    private InteractionPropertiesDocument interaction;
    private NodeConnectionPropertiesDocument connection;

    @Override
    public void setSubscription(SubscriptionDescriptor subscription) {}

    @Override
    public void setInteraction(InteractionDescriptor interaction) {}

    @Override
    public void setConnection(NodeConnectionDescriptor connection) {}
}
