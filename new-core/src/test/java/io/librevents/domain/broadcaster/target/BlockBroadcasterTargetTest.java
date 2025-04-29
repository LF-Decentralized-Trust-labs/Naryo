package io.librevents.domain.broadcaster.target;

import io.librevents.domain.broadcaster.AbstractBroadcasterTargetTest;
import io.librevents.domain.broadcaster.BroadcasterTarget;
import io.librevents.domain.broadcaster.Destination;

class BlockBroadcasterTargetTest extends AbstractBroadcasterTargetTest {

    @Override
    protected BroadcasterTarget createBroadcasterTarget(Destination destination) {
        return new BlockBroadcasterTarget(destination);
    }
}
