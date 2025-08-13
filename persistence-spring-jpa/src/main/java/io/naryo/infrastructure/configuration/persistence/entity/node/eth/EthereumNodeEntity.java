package io.naryo.infrastructure.configuration.persistence.entity.node.eth;

import java.util.UUID;

import io.naryo.application.configuration.source.model.node.EthereumNodeDescriptor;
import io.naryo.infrastructure.configuration.persistence.entity.node.NodeEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.connection.ConnectionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.interaction.InteractionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.subscription.SubscriptionEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("ethereum")
@NoArgsConstructor
public abstract class EthereumNodeEntity extends NodeEntity implements EthereumNodeDescriptor {

    public EthereumNodeEntity(
            UUID id,
            @Nullable String name,
            @Nullable SubscriptionEntity subscription,
            @Nullable InteractionEntity interaction,
            @Nullable ConnectionEntity connection) {
        super(id, name, subscription, interaction, connection);
    }
}
