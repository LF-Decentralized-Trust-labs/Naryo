package io.naryo.api.broadcaster.create.model;

import java.util.UUID;

import io.naryo.api.broadcaster.common.request.BroadcasterTargetDTO;
import io.naryo.domain.broadcaster.Broadcaster;
import jakarta.validation.constraints.NotNull;

public record CreateBroadcasterRequest(
        @NotNull BroadcasterTargetDTO target, @NotNull UUID configurationId) {

    public Broadcaster toDomain() {
        return new Broadcaster(UUID.randomUUID(), target.toDomain(), configurationId);
    }
}
