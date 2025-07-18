package io.naryo.infrastructure.configuration.persistence.document.node.interaction.block;

import io.naryo.application.configuration.source.model.node.interaction.BlockInteractionDescriptor;
import io.naryo.domain.node.interaction.block.InteractionMode;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.InteractionPropertiesDocument;
import lombok.Getter;

@Getter
public abstract class BlockInteractionPropertiesDocument extends InteractionPropertiesDocument
        implements BlockInteractionDescriptor {
    private InteractionMode mode;
}
