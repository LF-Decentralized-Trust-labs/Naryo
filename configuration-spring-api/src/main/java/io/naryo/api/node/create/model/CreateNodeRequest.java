package io.naryo.api.node.create.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeName;
import io.naryo.domain.node.NodeType;
import io.naryo.domain.node.ethereum.priv.GroupId;
import io.naryo.domain.node.ethereum.priv.PrecompiledAddress;
import io.naryo.domain.node.ethereum.priv.PrivateEthereumNode;
import io.naryo.domain.node.ethereum.pub.PublicEthereumNode;
import io.naryo.domain.node.hedera.HederaNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = EthereumNodeRequest.class, name = "ETHEREUM"),
    @JsonSubTypes.Type(value = HederaNodeRequest.class, name = "HEDERA"),
})
public abstract class CreateNodeRequest {

    private final @NotBlank String name;
    private final @NotNull NodeType type;
    private final @NotNull SubscriptionConfigurationRequest subscription;
    private final @NotNull InteractionConfigurationRequest interaction;
    private final @NotNull NodeConnectionRequest connection;

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

    public Node toDomain() {
        return switch (this.type) {
            case ETHEREUM -> {
                var ethNode = (EthereumNodeRequest) this;
                yield switch (ethNode.getVisibility()) {
                    case PUBLIC ->
                            new PublicEthereumNode(
                                    UUID.randomUUID(),
                                    new NodeName(this.name),
                                    this.subscription.toDomain(),
                                    this.interaction.toDomain(),
                                    this.connection.toDomain());
                    case PRIVATE ->
                            new PrivateEthereumNode(
                                    UUID.randomUUID(),
                                    new NodeName(this.name),
                                    this.subscription.toDomain(),
                                    this.interaction.toDomain(),
                                    this.connection.toDomain(),
                                    new GroupId(((PrivateEthereumNodeRequest) this).getGroupId()),
                                    new PrecompiledAddress(
                                            ((PrivateEthereumNodeRequest) this)
                                                    .getPrecompiledAddress()));
                };
            }
            case HEDERA ->
                    new HederaNode(
                            UUID.randomUUID(),
                            new NodeName(this.name),
                            this.subscription.toDomain(),
                            this.interaction.toDomain(),
                            this.connection.toDomain());
        };
    }
}
