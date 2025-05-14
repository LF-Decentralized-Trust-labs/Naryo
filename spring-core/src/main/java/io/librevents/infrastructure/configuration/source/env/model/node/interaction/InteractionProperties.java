package io.librevents.infrastructure.configuration.source.env.model.node.interaction;

import io.librevents.domain.node.interaction.InteractionStrategy;

public record InteractionProperties(
        InteractionStrategy strategy, InteractionConfigurationProperties configuration) {}
