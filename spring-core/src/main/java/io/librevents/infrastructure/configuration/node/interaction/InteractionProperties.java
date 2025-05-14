package io.librevents.infrastructure.configuration.node.interaction;

import io.librevents.domain.node.interaction.InteractionStrategy;

public record InteractionProperties(
        InteractionStrategy strategy, InteractionConfigurationProperties configuration) {}
