package io.librevents.infrastructure.configuration.node.interaction.block;

public record HederaMirrorNodeBlockInteractionModeConfigurationProperties(
        Integer limitPerRequest, Integer retriesPerRequest)
        implements BlockInteractionModeConfigurationProperties {}
