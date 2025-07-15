package io.naryo.infrastructure.configuration.persistence.document.node.interaction.block;

import io.naryo.domain.node.interaction.block.InteractionMode;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.InteractionPropertiesDocument;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("block_interaction_properties")
@Getter
public class BlockInteractionPropertiesDocument extends InteractionPropertiesDocument {
    private InteractionMode mode;
    private BlockInteractionModePropertiesDocument configuration;
}
