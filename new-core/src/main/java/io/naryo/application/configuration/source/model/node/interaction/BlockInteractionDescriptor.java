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
        var interaction = InteractionDescriptor.super.merge(other);

        if (interaction instanceof BlockInteractionDescriptor otherDescriptor) {
            if (!this.getMode().equals(otherDescriptor.getMode())) {
                return interaction;
            }
        }

        return interaction;
    }
}
