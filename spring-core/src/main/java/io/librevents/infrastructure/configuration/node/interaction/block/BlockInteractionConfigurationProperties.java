package io.librevents.infrastructure.configuration.node.interaction.block;

import io.librevents.domain.node.interaction.block.InteractionMode;
import io.librevents.infrastructure.configuration.node.interaction.InteractionConfigurationProperties;

public record BlockInteractionConfigurationProperties(
        InteractionMode mode, BlockInteractionModeConfigurationProperties configuration)
        implements InteractionConfigurationProperties {}
