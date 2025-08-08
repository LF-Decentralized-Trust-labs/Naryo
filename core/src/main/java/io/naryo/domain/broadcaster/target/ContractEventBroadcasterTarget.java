package io.naryo.domain.broadcaster.target;

import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import io.naryo.domain.common.Destination;

public final class ContractEventBroadcasterTarget extends BroadcasterTarget {

    public ContractEventBroadcasterTarget(Destination destination) {
        super(BroadcasterTargetType.CONTRACT_EVENT, destination);
    }
}
