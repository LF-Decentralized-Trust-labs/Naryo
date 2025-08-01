package io.naryo.infrastructure.configuration.persistence.entity.node.hedera;

import io.naryo.application.configuration.source.model.node.HederaNodeDescriptor;
import io.naryo.infrastructure.configuration.persistence.entity.node.NodeEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.connection.ConnectionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.interaction.InteractionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.subscription.SubscriptionEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.UUID;

@Entity
@DiscriminatorValue("hedera")
public abstract class HederaNodeEntity extends NodeEntity implements HederaNodeDescriptor {

    public HederaNodeEntity(UUID id, @Nullable String name,
                            @Nullable SubscriptionEntity subscription,
                            @Nullable InteractionEntity interaction,
                            @Nullable ConnectionEntity connection) {
        super(id, name, subscription, interaction, connection);
    }

    // Required by JPA
    protected HederaNodeEntity() {
        this(null, null, null, null, null);
    }
}
