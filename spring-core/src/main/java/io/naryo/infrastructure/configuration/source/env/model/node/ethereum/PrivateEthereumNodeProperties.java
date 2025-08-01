package io.naryo.infrastructure.configuration.source.env.model.node.ethereum;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.node.PrivateEthereumNodeDescriptor;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import jakarta.annotation.Nullable;
import lombok.Setter;

@Setter
public final class PrivateEthereumNodeProperties extends EthereumNodeProperties
        implements PrivateEthereumNodeDescriptor {

    private @Nullable String groupId;
    private @Nullable String precompiledAddress;

    public PrivateEthereumNodeProperties(
            UUID id,
            String name,
            SubscriptionDescriptor subscription,
            InteractionDescriptor interaction,
            NodeConnectionDescriptor connection,
            String groupId,
            String precompiledAddress) {
        super(id, name, subscription, interaction, connection);
        this.groupId = groupId;
        this.precompiledAddress = precompiledAddress;
    }

    @Override
    public Optional<String> getGroupId() {
        return Optional.ofNullable(groupId);
    }

    @Override
    public Optional<String> getPrecompiledAddress() {
        return Optional.ofNullable(precompiledAddress);
    }
}
