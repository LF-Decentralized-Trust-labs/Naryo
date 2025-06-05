package io.librevents.infrastructure.configuration.source.env.model.node.interaction;

import io.librevents.domain.node.interaction.InteractionStrategy;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record InteractionProperties(
        @NotNull InteractionStrategy strategy,
        @Valid @NotNull InteractionConfigurationProperties configuration) {}
