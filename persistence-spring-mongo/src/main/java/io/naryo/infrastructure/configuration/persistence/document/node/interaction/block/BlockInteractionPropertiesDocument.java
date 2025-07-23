package io.naryo.infrastructure.configuration.persistence.document.node.interaction.block;

import io.naryo.application.configuration.source.model.node.interaction.BlockInteractionDescriptor;
import io.naryo.domain.node.interaction.InteractionStrategy;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.InteractionPropertiesDocument;

public abstract class BlockInteractionPropertiesDocument extends InteractionPropertiesDocument
        implements BlockInteractionDescriptor {

    @Override
    public InteractionStrategy getStrategy() {
        return InteractionStrategy.BLOCK_BASED;
    }
}
