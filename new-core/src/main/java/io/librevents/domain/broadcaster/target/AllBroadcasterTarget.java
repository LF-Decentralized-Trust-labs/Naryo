package io.librevents.domain.broadcaster.target;

import io.librevents.domain.broadcaster.BroadcasterTarget;
import io.librevents.domain.broadcaster.BroadcasterTargetType;
import io.librevents.domain.broadcaster.Destination;

public class AllBroadcasterTarget extends BroadcasterTarget {

    public AllBroadcasterTarget(Destination destination) {
        super(BroadcasterTargetType.ALL, destination);
    }
}
