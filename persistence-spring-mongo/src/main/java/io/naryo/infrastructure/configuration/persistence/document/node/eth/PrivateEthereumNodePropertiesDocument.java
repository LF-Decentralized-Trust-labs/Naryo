package io.naryo.infrastructure.configuration.persistence.document.node.eth;

import java.util.Optional;

import io.naryo.application.configuration.source.model.node.PrivateEthereumNodeDescriptor;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import io.naryo.infrastructure.configuration.persistence.document.node.connection.ConnectionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.InteractionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.subscription.SubscriptionPropertiesDocument;
import jakarta.annotation.Nullable;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

@Setter
@TypeAlias("private_ethereum_node")
public final class PrivateEthereumNodePropertiesDocument extends EthereumNodePropertiesDocument
        implements PrivateEthereumNodeDescriptor {

    private @Nullable String groupId;
    private @Nullable String precompiledAddress;

    public PrivateEthereumNodePropertiesDocument(
            String id,
            String name,
            SubscriptionPropertiesDocument subscription,
            InteractionPropertiesDocument interaction,
            ConnectionPropertiesDocument connection,
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
