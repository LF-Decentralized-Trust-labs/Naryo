package io.naryo.infrastructure.configuration.persistence.entity.node.interaction.block;

import io.naryo.application.configuration.source.model.node.interaction.BlockInteractionDescriptor;
import io.naryo.domain.node.interaction.InteractionStrategy;
import io.naryo.infrastructure.configuration.persistence.entity.node.interaction.InteractionEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("block")
public abstract class BlockInteractionEntity extends InteractionEntity
        implements BlockInteractionDescriptor {
    @Override
    public InteractionStrategy getStrategy() {
        return InteractionStrategy.BLOCK_BASED;
    }
}
