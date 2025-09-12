package io.naryo.infrastructure.configuration.persistence.entity.node.eth;

import java.util.UUID;

import io.naryo.application.configuration.source.model.node.PublicEthereumNodeDescriptor;
import io.naryo.infrastructure.configuration.persistence.entity.node.connection.ConnectionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.interaction.InteractionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.subscription.SubscriptionEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("public_ethereum")
@NoArgsConstructor
public class PublicEthereumNodeEntity extends EthereumNodeEntity
        implements PublicEthereumNodeDescriptor {

    public PublicEthereumNodeEntity(
            UUID id,
            String name,
            SubscriptionEntity subscription,
            InteractionEntity interaction,
            ConnectionEntity connection) {
        super(id, name, subscription, interaction, connection);
    }
}
