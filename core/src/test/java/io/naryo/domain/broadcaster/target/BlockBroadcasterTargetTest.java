package io.naryo.domain.broadcaster.target;

import java.util.List;

import io.naryo.domain.broadcaster.AbstractBroadcasterTargetTest;
import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.common.Destination;

class BlockBroadcasterTargetTest extends AbstractBroadcasterTargetTest {

    @Override
    protected BroadcasterTarget createBroadcasterTarget(List<Destination> destinations) {
        return new BlockBroadcasterTarget(destinations);
    }
}
