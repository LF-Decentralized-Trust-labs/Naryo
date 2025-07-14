package io.naryo.application.configuration.source.model.node.interaction;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.node.interaction.InteractionStrategy;

public interface InteractionDescriptor extends MergeableDescriptor<InteractionDescriptor> {

    InteractionStrategy getStrategy();

    @Override
    default InteractionDescriptor merge(InteractionDescriptor other) {
        if (other == null) {
            return this;
        }

        if (!this.getStrategy().equals(other.getStrategy())) {
            return other;
        }

        return this;
    }
}
