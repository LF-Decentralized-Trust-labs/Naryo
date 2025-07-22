package io.naryo.infrastructure.configuration.persistence.document.node.interaction.block;

import io.naryo.application.configuration.source.model.node.interaction.BlockInteractionDescriptor;
import io.naryo.domain.node.interaction.InteractionStrategy;
import io.naryo.domain.node.interaction.block.InteractionMode;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.InteractionPropertiesDocument;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public abstract class BlockInteractionPropertiesDocument extends InteractionPropertiesDocument
        implements BlockInteractionDescriptor {

    private final @Getter @NotNull InteractionMode mode;

    public BlockInteractionPropertiesDocument(InteractionMode mode) {
        super(InteractionStrategy.BLOCK_BASED);
        this.mode = mode;
    }
}
