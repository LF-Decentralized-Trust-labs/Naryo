package io.librevents.domain.broadcaster.target;

import io.librevents.domain.broadcaster.BroadcasterTarget;
import io.librevents.domain.broadcaster.BroadcasterTargetType;
import io.librevents.domain.broadcaster.Destination;

public final class BlockBroadcasterTarget extends BroadcasterTarget {

    public BlockBroadcasterTarget(Destination destination) {
        super(BroadcasterTargetType.BLOCK, destination);
    }
}
