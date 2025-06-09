package io.naryo.infrastructure.configuration.source.env.model.node.interaction;

import io.naryo.domain.node.interaction.InteractionStrategy;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record InteractionProperties(
        @NotNull InteractionStrategy strategy,
        @Valid @NotNull InteractionConfigurationProperties configuration) {}
