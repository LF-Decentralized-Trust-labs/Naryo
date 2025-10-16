package io.naryo.api.node.update.model;

import java.util.UUID;

import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public abstract class UpdateNodeRequest {

    protected final @NotBlank String name;
    protected final @NotNull NodeType type;
    protected final @NotNull SubscriptionConfigurationRequest subscription;
    protected final @NotNull InteractionConfigurationRequest interaction;
    protected final @NotNull NodeConnectionRequest connection;
    protected final @NotBlank String prevItemHash;

    protected UpdateNodeRequest(
            String name,
            NodeType type,
            SubscriptionConfigurationRequest subscription,
            InteractionConfigurationRequest interaction,
            NodeConnectionRequest connection,
            String prevItemHash) {
        this.name = name;
        this.type = type;
        this.subscription = subscription;
        this.interaction = interaction;
        this.connection = connection;
        this.prevItemHash = prevItemHash;
    }

    public abstract Node toDomain(UUID nodeId);
}
