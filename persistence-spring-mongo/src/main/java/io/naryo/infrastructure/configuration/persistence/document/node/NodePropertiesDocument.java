package io.naryo.infrastructure.configuration.persistence.document.node;

import java.util.UUID;

import io.naryo.domain.node.NodeType;
import io.naryo.infrastructure.configuration.persistence.document.node.connection.ConnectionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.InteractionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.subscription.SubscriptionPropertiesDocument;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "nodes")
@Getter
public abstract class NodePropertiesDocument {
    @MongoId private UUID id;
    @NotBlank private String name;
    @NotNull private NodeType type;
    private SubscriptionPropertiesDocument subscription;
    private InteractionPropertiesDocument interaction;
    private ConnectionPropertiesDocument connection;
}
