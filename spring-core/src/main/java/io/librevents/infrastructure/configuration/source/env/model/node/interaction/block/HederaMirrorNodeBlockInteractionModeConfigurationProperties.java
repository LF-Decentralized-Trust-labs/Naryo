package io.librevents.infrastructure.configuration.source.env.model.node.interaction.block;

public record HederaMirrorNodeBlockInteractionModeConfigurationProperties(
        Integer limitPerRequest, Integer retriesPerRequest)
        implements BlockInteractionModeConfigurationProperties {}
