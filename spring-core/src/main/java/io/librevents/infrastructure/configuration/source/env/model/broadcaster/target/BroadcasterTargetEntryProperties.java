package io.librevents.infrastructure.configuration.source.env.model.broadcaster.target;

import java.util.UUID;

import io.librevents.domain.broadcaster.BroadcasterTargetType;

public record BroadcasterTargetEntryProperties(
        UUID configurationId,
        BroadcasterTargetType type,
        String destination,
        BroadcasterTargetAdditionalProperties configuration) {}
