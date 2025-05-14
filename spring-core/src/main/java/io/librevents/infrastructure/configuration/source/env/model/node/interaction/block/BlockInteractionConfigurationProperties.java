package io.librevents.infrastructure.configuration.source.env.model.node.interaction.block;

import io.librevents.domain.node.interaction.block.InteractionMode;
import io.librevents.infrastructure.configuration.source.env.model.node.interaction.InteractionConfigurationProperties;

public record BlockInteractionConfigurationProperties(
        InteractionMode mode, BlockInteractionModeConfigurationProperties configuration)
        implements InteractionConfigurationProperties {}
