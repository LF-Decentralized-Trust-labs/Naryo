package io.naryo.infrastructure.configuration.source.env.model.node.interaction.block;

import io.naryo.domain.node.interaction.InteractionStrategy;
import io.naryo.domain.node.interaction.block.InteractionMode;
import io.naryo.infrastructure.configuration.source.env.model.node.interaction.InteractionProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public abstract class BlockInteractionProperties extends InteractionProperties {

    private final @Getter @NotNull InteractionMode mode;

    public BlockInteractionProperties(InteractionMode mode) {
        super(InteractionStrategy.BLOCK_BASED);
        this.mode = mode;
    }
}
