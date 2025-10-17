package io.naryo.api.node.common.request;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = EthereumNodeRequest.class, name = "ETHEREUM"),
    @JsonSubTypes.Type(value = HederaNodeRequest.class, name = "HEDERA"),
})
@Getter
public abstract class NodeRequest {

    protected final @NotBlank String name;
    protected final @NotNull NodeType type;
    protected final @NotNull SubscriptionConfigurationRequest subscription;
    protected final @NotNull InteractionConfigurationRequest interaction;
    protected final @NotNull NodeConnectionRequest connection;

    protected NodeRequest(
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

    public abstract Node toDomain(UUID nodeId);
}
