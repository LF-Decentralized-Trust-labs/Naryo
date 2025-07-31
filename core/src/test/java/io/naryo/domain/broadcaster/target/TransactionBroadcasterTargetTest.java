package io.naryo.domain.broadcaster.target;

import io.naryo.domain.broadcaster.AbstractBroadcasterTargetTest;
import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.Destination;

class TransactionBroadcasterTargetTest extends AbstractBroadcasterTargetTest {

    @Override
    protected BroadcasterTarget createBroadcasterTarget(Destination destination) {
        return new TransactionBroadcasterTarget(destination);
    }
}
