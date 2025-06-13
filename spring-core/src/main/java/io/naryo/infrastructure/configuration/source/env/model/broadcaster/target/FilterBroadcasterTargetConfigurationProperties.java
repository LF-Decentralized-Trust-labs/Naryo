package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record FilterBroadcasterTargetConfigurationProperties(@NotNull UUID filterId)
        implements BroadcasterTargetAdditionalProperties {}
