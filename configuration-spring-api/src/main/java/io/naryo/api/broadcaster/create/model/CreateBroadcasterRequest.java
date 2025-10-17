package io.naryo.api.broadcaster.create.model;

import java.util.UUID;

import io.naryo.api.broadcaster.common.request.BroadcasterTargetDTO;
import io.naryo.domain.broadcaster.Broadcaster;
import jakarta.validation.constraints.NotNull;

public record CreateBroadcasterRequest(
        @NotNull UUID id, @NotNull BroadcasterTargetDTO target, @NotNull UUID configurationId) {

    public CreateBroadcasterRequest(BroadcasterTargetDTO target, UUID configurationId) {
        this(UUID.randomUUID(), target, configurationId);
    }

    public Broadcaster toDomain() {
        return new Broadcaster(id, target.toDomain(), configurationId);
    }
}
