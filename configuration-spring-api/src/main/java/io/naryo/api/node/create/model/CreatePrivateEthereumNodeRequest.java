package io.naryo.api.node.create.model;

import java.util.UUID;

import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeName;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import io.naryo.domain.node.ethereum.priv.GroupId;
import io.naryo.domain.node.ethereum.priv.PrecompiledAddress;
import io.naryo.domain.node.ethereum.priv.PrivateEthereumNode;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public final class CreatePrivateEthereumNodeRequest extends CreateEthereumNodeRequest {

    private final @NotBlank String groupId;
    private final @NotBlank String precompiledAddress;

    public CreatePrivateEthereumNodeRequest(
            String name,
            SubscriptionConfigurationRequest subscription,
            InteractionConfigurationRequest interaction,
            NodeConnectionRequest connection,
            String groupId,
            String precompiledAddress) {
        super(name, subscription, interaction, connection, EthereumNodeVisibility.PRIVATE);
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
                new GroupId(this.getGroupId()),
                new PrecompiledAddress(this.getPrecompiledAddress()));
    }
}
