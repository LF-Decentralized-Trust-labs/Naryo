package io.naryo.infrastructure.configuration.source.env.model.node;

import java.util.UUID;

import io.naryo.application.configuration.source.model.node.NodeDescriptor;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.domain.node.NodeType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public abstract class NodeProperties implements NodeDescriptor {

    private final @Getter @NotNull UUID id;
    private final @Getter @NotNull NodeType type;
    private @Getter @Setter @NotBlank String name;
    private @Getter @Setter @Valid @NotNull SubscriptionDescriptor subscription;
    private @Getter @Setter @Valid @NotNull InteractionDescriptor interaction;
    private @Getter @Setter @Valid @NotNull NodeConnectionDescriptor connection;

    protected NodeProperties(
            UUID id,
            String name,
            NodeType type,
            SubscriptionDescriptor subscription,
            InteractionDescriptor interaction,
            NodeConnectionDescriptor connection) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.subscription = subscription;
        this.interaction = interaction;
        this.connection = connection;
    }
}
