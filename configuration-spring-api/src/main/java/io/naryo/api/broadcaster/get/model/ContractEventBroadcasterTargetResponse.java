package io.naryo.api.broadcaster.get.model;

import java.util.List;

import io.naryo.domain.broadcaster.target.ContractEventBroadcasterTarget;
import io.naryo.domain.common.Destination;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Contract event broadcaster target")
@Getter
public final class ContractEventBroadcasterTargetResponse extends BroadcasterTargetResponse {

    private ContractEventBroadcasterTargetResponse(List<Destination> destinations) {
        super(destinations);
    }

    public static ContractEventBroadcasterTargetResponse fromDomain(
            ContractEventBroadcasterTarget contractEventBroadcasterTarget) {
        return new ContractEventBroadcasterTargetResponse(
                contractEventBroadcasterTarget.getDestinations());
    }
}
