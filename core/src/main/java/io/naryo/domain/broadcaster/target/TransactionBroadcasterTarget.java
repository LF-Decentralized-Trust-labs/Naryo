package io.naryo.domain.broadcaster.target;

import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import io.naryo.domain.broadcaster.Destination;

public final class TransactionBroadcasterTarget extends BroadcasterTarget {

    public TransactionBroadcasterTarget(Destination destination) {
        super(BroadcasterTargetType.TRANSACTION, destination);
    }
}
