package io.librevents.domain.broadcaster.target;

import io.librevents.domain.broadcaster.BroadcasterTarget;
import io.librevents.domain.broadcaster.BroadcasterTargetType;
import io.librevents.domain.broadcaster.Destination;

public class TransactionBroadcasterTarget extends BroadcasterTarget {

    protected TransactionBroadcasterTarget(Destination destination) {
        super(BroadcasterTargetType.TRANSACTION, destination);
    }
}
