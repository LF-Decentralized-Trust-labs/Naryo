package io.naryo.application.configuration.source.model.node.interaction;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.node.interaction.InteractionStrategy;

public interface InteractionDescriptor extends MergeableDescriptor<InteractionDescriptor> {

    InteractionStrategy getStrategy();

    @Override
    default InteractionDescriptor merge(InteractionDescriptor other) {
        return this;
    }
}
