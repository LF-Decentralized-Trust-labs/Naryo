package io.naryo.api.node.common.request;

import java.util.UUID;

import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeName;
import io.naryo.domain.node.ethereum.priv.GroupId;
import io.naryo.domain.node.ethereum.priv.PrecompiledAddress;
import io.naryo.domain.node.ethereum.priv.PrivateEthereumNode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Schema(description = "Private Ethereum node")
@Getter
public final class PrivateEthereumNodeRequest extends EthereumNodeRequest {

    private final @NotBlank String groupId;
    private final @NotBlank String precompiledAddress;

    public PrivateEthereumNodeRequest(
            String name,
            SubscriptionConfigurationRequest subscription,
            InteractionConfigurationRequest interaction,
            NodeConnectionRequest connection,
            String groupId,
            String precompiledAddress) {
        super(name, subscription, interaction, connection);
        this.groupId = groupId;
        this.precompiledAddress = precompiledAddress;
    }

    @Override
    public Node toDomain() {
        return new PrivateEthereumNode(
                UUID.randomUUID(),
                new NodeName(this.name),
                this.subscription.toDomain(),
                this.interaction.toDomain(),
                this.connection.toDomain(),
                new GroupId(groupId),
                new PrecompiledAddress(precompiledAddress));
    }

    @Override
    public Node toDomain(UUID nodeId) {
        return new PrivateEthereumNode(
                nodeId,
                new NodeName(this.name),
                this.subscription.toDomain(),
                this.interaction.toDomain(),
                this.connection.toDomain(),
                new GroupId(this.groupId),
                new PrecompiledAddress(this.precompiledAddress));
    }
}
