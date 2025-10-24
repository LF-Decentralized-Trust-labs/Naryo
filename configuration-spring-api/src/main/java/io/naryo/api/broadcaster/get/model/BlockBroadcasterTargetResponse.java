package io.naryo.api.broadcaster.get.model;

import java.util.List;

import io.naryo.domain.broadcaster.target.BlockBroadcasterTarget;
import io.naryo.domain.common.Destination;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Block broadcaster target")
@Getter
public final class BlockBroadcasterTargetResponse extends BroadcasterTargetResponse {

    private BlockBroadcasterTargetResponse(List<Destination> destinations) {
        super(destinations);
    }

    public static BlockBroadcasterTargetResponse fromDomain(
            BlockBroadcasterTarget blockBroadcasterTarget) {
        return new BlockBroadcasterTargetResponse(blockBroadcasterTarget.getDestinations());
    }
}
