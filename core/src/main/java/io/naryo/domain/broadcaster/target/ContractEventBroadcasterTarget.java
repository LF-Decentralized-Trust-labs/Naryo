package io.naryo.domain.broadcaster.target;

import java.util.List;

import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import io.naryo.domain.common.Destination;

public final class ContractEventBroadcasterTarget extends BroadcasterTarget {

    public ContractEventBroadcasterTarget(List<Destination> destinations) {
        super(BroadcasterTargetType.CONTRACT_EVENT, destinations);
    }
}
