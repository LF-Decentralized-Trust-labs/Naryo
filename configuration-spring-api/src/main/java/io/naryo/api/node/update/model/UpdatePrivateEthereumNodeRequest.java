package io.naryo.api.node.update.model;

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
public final class UpdatePrivateEthereumNodeRequest extends UpdateEthereumNodeRequest {

    private final @NotBlank String groupId;
    private final @NotBlank String precompiledAddress;

    public UpdatePrivateEthereumNodeRequest(
            String name,
            SubscriptionConfigurationRequest subscription,
            InteractionConfigurationRequest interaction,
            NodeConnectionRequest connection,
            String groupId,
            String precompiledAddress,
            String prevItemHash) {
        super(
                name,
                subscription,
                interaction,
                connection,
                EthereumNodeVisibility.PRIVATE,
                prevItemHash);
        this.groupId = groupId;
        this.precompiledAddress = precompiledAddress;
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
