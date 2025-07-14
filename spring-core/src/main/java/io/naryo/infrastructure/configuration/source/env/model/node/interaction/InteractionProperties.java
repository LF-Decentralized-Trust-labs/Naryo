package io.naryo.infrastructure.configuration.source.env.model.node.interaction;

import io.naryo.domain.node.interaction.InteractionStrategy;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public abstract class InteractionProperties {

    private final @Getter @NotNull InteractionStrategy strategy;

    public InteractionProperties(InteractionStrategy strategy) {
        this.strategy = strategy;
    }
}
