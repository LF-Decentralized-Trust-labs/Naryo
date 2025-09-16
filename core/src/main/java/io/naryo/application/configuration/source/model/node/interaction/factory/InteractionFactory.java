package io.naryo.application.configuration.source.model.node.interaction.factory;

import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.domain.node.interaction.InteractionConfiguration;

public interface InteractionFactory {
    InteractionConfiguration create(InteractionDescriptor descriptor);
}
