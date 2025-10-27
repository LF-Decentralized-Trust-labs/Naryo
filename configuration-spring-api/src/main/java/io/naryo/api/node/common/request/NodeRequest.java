package io.naryo.api.node.common.request;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.naryo.domain.node.Node;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = PublicEthereumNodeRequest.class, name = "ETHEREUM_PUBLIC"),
    @JsonSubTypes.Type(value = PrivateEthereumNodeRequest.class, name = "ETHEREUM_PRIVATE"),
    @JsonSubTypes.Type(value = HederaNodeRequest.class, name = "HEDERA"),
})
@Schema(
        description = "Base class for node request",
        discriminatorProperty = "type",
        discriminatorMapping = {
            @DiscriminatorMapping(
                    value = "ETHEREUM_PUBLIC",
                    schema = PublicEthereumNodeRequest.class),
            @DiscriminatorMapping(
                    value = "ETHEREUM_PRIVATE",
                    schema = PrivateEthereumNodeRequest.class),
            @DiscriminatorMapping(value = "HEDERA", schema = HederaNodeRequest.class)
        })
@Getter
public abstract class NodeRequest {

    protected final @NotBlank String name;
    protected final @NotNull SubscriptionConfigurationRequest subscription;
    protected final @NotNull InteractionConfigurationRequest interaction;
    protected final @NotNull NodeConnectionRequest connection;

    protected NodeRequest(
            String name,
            SubscriptionConfigurationRequest subscription,
            InteractionConfigurationRequest interaction,
            NodeConnectionRequest connection) {
        this.name = name;
        this.subscription = subscription;
        this.interaction = interaction;
        this.connection = connection;
    }

    public abstract Node toDomain();

    public abstract Node toDomain(UUID nodeId);
}
