package io.naryo.api.broadcaster.common.request;

import java.util.List;

import io.naryo.domain.broadcaster.BroadcasterTargetType;
import jakarta.validation.constraints.NotEmpty;

public final class ContractEventBroadcasterTargetDTO extends BroadcasterTargetDTO {
    public ContractEventBroadcasterTargetDTO(@NotEmpty List<String> destinations) {
        super(BroadcasterTargetType.CONTRACT_EVENT, destinations);
    }
}
