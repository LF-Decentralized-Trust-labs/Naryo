package io.naryo.api.broadcaster.get.model;

import java.util.List;

import io.naryo.domain.broadcaster.target.AllBroadcasterTarget;
import io.naryo.domain.common.Destination;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "All broadcaster")
@Getter
public final class AllBroadcasterTargetResponse extends BroadcasterTargetResponse {

    private AllBroadcasterTargetResponse(List<Destination> destinations) {
        super(destinations);
    }

    public static AllBroadcasterTargetResponse fromDomain(
            AllBroadcasterTarget allBroadcasterTarget) {
        return new AllBroadcasterTargetResponse(allBroadcasterTarget.getDestinations());
    }
}
