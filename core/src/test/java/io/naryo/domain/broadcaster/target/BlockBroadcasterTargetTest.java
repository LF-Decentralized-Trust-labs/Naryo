package io.naryo.domain.broadcaster.target;

import io.naryo.domain.broadcaster.AbstractBroadcasterTargetTest;
import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.common.Destination;

class BlockBroadcasterTargetTest extends AbstractBroadcasterTargetTest {

    @Override
    protected BroadcasterTarget createBroadcasterTarget(Destination destination) {
        return new BlockBroadcasterTarget(destination);
    }
}
