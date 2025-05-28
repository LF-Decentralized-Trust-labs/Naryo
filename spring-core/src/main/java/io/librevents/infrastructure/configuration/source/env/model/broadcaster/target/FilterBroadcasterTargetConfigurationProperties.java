package io.librevents.infrastructure.configuration.source.env.model.broadcaster.target;

import java.util.UUID;

public record FilterBroadcasterTargetConfigurationProperties(UUID filterId)
        implements BroadcasterTargetAdditionalProperties {}
