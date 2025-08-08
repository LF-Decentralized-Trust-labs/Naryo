package io.naryo.domain.broadcaster.target;

import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import io.naryo.domain.common.Destination;

public final class BlockBroadcasterTarget extends BroadcasterTarget {

    public BlockBroadcasterTarget(Destination destination) {
        super(BroadcasterTargetType.BLOCK, destination);
    }
}
