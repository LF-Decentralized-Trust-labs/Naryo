package io.librevents.infrastructure.configuration.broadcaster.target;

public record FilterBroadcasterTargetConfigurationProperties(String filterId)
        implements BroadcasterTargetAdditionalProperties {}
