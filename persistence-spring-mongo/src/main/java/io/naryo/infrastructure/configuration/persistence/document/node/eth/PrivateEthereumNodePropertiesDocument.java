package io.naryo.infrastructure.configuration.persistence.document.node.eth;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.node.PrivateEthereumNodeDescriptor;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import jakarta.annotation.Nullable;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("private_ethereum_node")
public final class PrivateEthereumNodePropertiesDocument extends EthereumNodePropertiesDocument
        implements PrivateEthereumNodeDescriptor {

    private @Setter @Nullable String groupId;
    private @Setter @Nullable String precompiledAddress;

    public PrivateEthereumNodePropertiesDocument(
            UUID id,
            String name,
            SubscriptionDescriptor subscription,
            InteractionDescriptor interaction,
            NodeConnectionDescriptor connection,
            String groupId,
            String precompiledAddress) {
        super(id, name, subscription, interaction, connection, EthereumNodeVisibility.PRIVATE);
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
