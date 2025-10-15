package io.naryo.api.node.create.model;

import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

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
        super(name, subscription, interaction, connection, EthereumNodeVisibility.PRIVATE);
        this.groupId = groupId;
        this.precompiledAddress = precompiledAddress;
    }
}
