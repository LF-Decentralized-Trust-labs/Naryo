package io.naryo.api.broadcaster.get.model;

import java.util.UUID;

import io.naryo.domain.broadcaster.Broadcaster;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Broadcaster")
@Getter
public class BroadcasterResponse {

    private final UUID id;
    private final BroadcasterTargetResponse target;
    private final String currentItemHash;

    private BroadcasterResponse(UUID id, BroadcasterTargetResponse target, String currentItemHash) {
        this.id = id;
        this.target = target;
        this.currentItemHash = currentItemHash;
    }

    public static BroadcasterResponse fromDomain(Broadcaster broadcaster, String currentItemHash) {
        return new BroadcasterResponse(
                broadcaster.getId(),
                BroadcasterTargetResponse.fromDomain(broadcaster.getTarget()),
                currentItemHash);
    }
}
