package io.naryo.api.broadcaster.common.request;

import java.util.UUID;

import io.naryo.domain.broadcaster.Broadcaster;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Broadcaster request")
public record BroadcasterRequest(
        @NotNull BroadcasterTargetRequest target, @NotNull UUID configurationId) {

    public Broadcaster toDomain() {
        return toDomain(UUID.randomUUID());
    }

    public Broadcaster toDomain(UUID id) {
        return new Broadcaster(id, target.toDomain(), configurationId);
    }
}
