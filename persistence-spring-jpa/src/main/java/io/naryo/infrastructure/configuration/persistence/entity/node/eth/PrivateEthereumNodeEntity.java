package io.naryo.infrastructure.configuration.persistence.entity.node.eth;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.node.PrivateEthereumNodeDescriptor;
import io.naryo.infrastructure.configuration.persistence.entity.node.connection.ConnectionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.interaction.InteractionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.subscription.SubscriptionEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@DiscriminatorValue("private")
@NoArgsConstructor
public class PrivateEthereumNodeEntity extends EthereumNodeEntity
        implements PrivateEthereumNodeDescriptor {

    private @Column(name = "groupd_id") @Nullable String groupId;

    private @Column(name = "precompiled_address") @Nullable String precompiledAddress;

    public PrivateEthereumNodeEntity(
            UUID id,
            String name,
            SubscriptionEntity subscription,
            InteractionEntity interaction,
            ConnectionEntity connection,
            String groupId,
            String precompiledAddress) {
        super(id, name, subscription, interaction, connection);
        this.groupId = groupId;
        this.precompiledAddress = precompiledAddress;
    }

    @Override
    public Optional<String> getGroupId() {
        return Optional.ofNullable(this.groupId);
    }

    @Override
    public Optional<String> getPrecompiledAddress() {
        return Optional.ofNullable(this.precompiledAddress);
    }
}
