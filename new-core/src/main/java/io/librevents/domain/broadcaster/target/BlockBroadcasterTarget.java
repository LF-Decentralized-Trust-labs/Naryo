package io.librevents.domain.broadcaster.target;

import io.librevents.domain.broadcaster.BroadcasterTarget;
import io.librevents.domain.broadcaster.BroadcasterTargetType;
import io.librevents.domain.broadcaster.Destination;

public class BlockBroadcasterTarget extends BroadcasterTarget {

    protected BlockBroadcasterTarget(Destination destination) {
        super(BroadcasterTargetType.BLOCK, destination);
    }
}
