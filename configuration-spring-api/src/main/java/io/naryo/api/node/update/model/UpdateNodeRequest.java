package io.naryo.api.node.update.model;

import java.util.UUID;

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
import lombok.Getter;

@Getter
public abstract class UpdateNodeRequest {

    private final @NotBlank String name;
    private final @NotNull NodeType type;
    private final @NotNull SubscriptionConfigurationRequest subscription;
    private final @NotNull InteractionConfigurationRequest interaction;
    private final @NotNull NodeConnectionRequest connection;
    private final @NotBlank String prevItemHash;

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

    public Node toDomain() {
        return switch (this.type) {
            case ETHEREUM -> {
                var ethNode = (UpdateEthereumNodeRequest) this;
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
                                    new GroupId(
                                            ((UpdatePrivateEthereumNodeRequest) this).getGroupId()),
                                    new PrecompiledAddress(
                                            ((UpdatePrivateEthereumNodeRequest) this)
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
