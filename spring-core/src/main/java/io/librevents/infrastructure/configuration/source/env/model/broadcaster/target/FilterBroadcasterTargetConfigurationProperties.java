package io.librevents.infrastructure.configuration.source.env.model.broadcaster.target;

public record FilterBroadcasterTargetConfigurationProperties(String filterId)
        implements BroadcasterTargetAdditionalProperties {}
