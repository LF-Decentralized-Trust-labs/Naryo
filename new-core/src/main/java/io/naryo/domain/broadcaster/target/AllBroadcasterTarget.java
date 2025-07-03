package io.naryo.domain.broadcaster.target;

import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import io.naryo.domain.broadcaster.Destination;

public final class AllBroadcasterTarget extends BroadcasterTarget {

    public AllBroadcasterTarget(Destination destination) {
        super(BroadcasterTargetType.ALL, destination);
    }
}
