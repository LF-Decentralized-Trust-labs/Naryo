package io.naryo.api.broadcaster.get.model;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import io.naryo.api.broadcaster.common.request.BroadcasterTargetDTO;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.broadcaster.BroadcasterTarget;

public record BroadcasterResponse(
        UUID id, BroadcasterTargetDTO target, UUID configurationId, String itemHash) {

    public static BroadcasterResponse map(Broadcaster broadcaster, Map<UUID, String> fingerprints) {
        Objects.requireNonNull(broadcaster, "broadcaster cannot be null");
        String hash = fingerprints.get(broadcaster.getId());
        BroadcasterTarget target = broadcaster.getTarget();

        return new BroadcasterResponse(
                broadcaster.getId(),
                BroadcasterTargetDTO.fromDomain(target),
                broadcaster.getConfigurationId(),
                hash);
    }
}
