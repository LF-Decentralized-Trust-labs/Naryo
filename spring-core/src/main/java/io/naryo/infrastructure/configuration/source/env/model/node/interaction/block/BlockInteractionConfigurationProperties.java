package io.naryo.infrastructure.configuration.source.env.model.node.interaction.block;

import io.naryo.domain.node.interaction.block.InteractionMode;
import io.naryo.infrastructure.configuration.source.env.model.node.interaction.InteractionConfigurationProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record BlockInteractionConfigurationProperties(
        @NotNull InteractionMode mode,
        @Valid @NotNull BlockInteractionModeConfigurationProperties configuration)
        implements InteractionConfigurationProperties {}
