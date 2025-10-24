package io.naryo.api.broadcaster.common.request;

import java.util.UUID;

import io.naryo.domain.broadcaster.Broadcaster;
import jakarta.validation.constraints.NotNull;

public record BroadcasterRequest(
        @NotNull BroadcasterTargetRequest target, @NotNull UUID configurationId) {

    public Broadcaster toDomain() {
        return toDomain(UUID.randomUUID());
    }

    public Broadcaster toDomain(UUID id) {
        return new Broadcaster(id, target.toDomain(), configurationId);
    }
}
