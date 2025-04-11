package io.librevents.domain.broadcaster.target;

import io.librevents.domain.broadcaster.BroadcasterTarget;
import io.librevents.domain.broadcaster.BroadcasterTargetType;
import io.librevents.domain.broadcaster.Destination;

public class ContractEventBroadcasterTarget extends BroadcasterTarget {

    protected ContractEventBroadcasterTarget(Destination destination) {
        super(BroadcasterTargetType.CONTRACT_EVENT, destination);
    }
}
