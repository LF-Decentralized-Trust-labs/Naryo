package io.naryo.application.configuration.source.model.node.interaction;

import io.naryo.domain.node.interaction.InteractionStrategy;
import io.naryo.domain.node.interaction.block.InteractionMode;

public interface BlockInteractionDescriptor extends InteractionDescriptor {

    InteractionMode getMode();

    @Override
    default InteractionStrategy getStrategy() {
        return InteractionStrategy.BLOCK_BASED;
    }

    @Override
    default InteractionDescriptor merge(InteractionDescriptor other) {
        if (!(other instanceof BlockInteractionDescriptor otherBlockInteraction)) {
            return this;
        }

        if (!this.getMode().equals(otherBlockInteraction.getMode())) {
            return this;
        }

        return InteractionDescriptor.super.merge(otherBlockInteraction);
    }
}
