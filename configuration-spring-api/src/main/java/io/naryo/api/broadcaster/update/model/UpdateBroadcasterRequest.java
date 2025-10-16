package io.naryo.api.broadcaster.update.model;

import java.util.UUID;

import io.naryo.api.broadcaster.common.request.BroadcasterTargetDTO;
import io.naryo.domain.broadcaster.Broadcaster;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateBroadcasterRequest(
        @NotNull BroadcasterTargetDTO target,
        @NotNull UUID configurationId,
        @NotBlank String prevItemHash) {

    public Broadcaster toDomain(@NotNull UUID id) {
        return new Broadcaster(id, target.toDomain(), configurationId);
    }
}
