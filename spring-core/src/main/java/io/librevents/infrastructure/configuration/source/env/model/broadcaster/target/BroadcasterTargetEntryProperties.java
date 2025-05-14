package io.librevents.infrastructure.configuration.source.env.model.broadcaster.target;

import io.librevents.domain.broadcaster.BroadcasterTargetType;

public record BroadcasterTargetEntryProperties(
        String configurationName,
        BroadcasterTargetType type,
        String destination,
        BroadcasterTargetAdditionalProperties configuration) {}
