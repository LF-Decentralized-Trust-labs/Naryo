package io.naryo.api.node.create.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CreateEthereumNodeRequest.class, name = "ETHEREUM"),
    @JsonSubTypes.Type(value = CreateHederaNodeRequest.class, name = "HEDERA"),
})
public abstract class CreateNodeRequest {

    protected final @NotBlank String name;
    protected final @NotNull NodeType type;
    protected final @NotNull SubscriptionConfigurationRequest subscription;
    protected final @NotNull InteractionConfigurationRequest interaction;
    protected final @NotNull NodeConnectionRequest connection;

    protected CreateNodeRequest(
            String name,
            NodeType type,
            SubscriptionConfigurationRequest subscription,
            InteractionConfigurationRequest interaction,
            NodeConnectionRequest connection) {
        this.name = name;
        this.type = type;
        this.subscription = subscription;
        this.interaction = interaction;
        this.connection = connection;
    }

    public abstract Node toDomain();
}
