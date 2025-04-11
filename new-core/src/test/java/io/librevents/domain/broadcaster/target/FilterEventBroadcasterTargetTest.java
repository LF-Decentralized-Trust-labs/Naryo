package io.librevents.domain.broadcaster.target;

import java.util.UUID;

import io.librevents.domain.broadcaster.AbstractBroadcasterTargetTest;
import io.librevents.domain.broadcaster.BroadcasterTarget;
import io.librevents.domain.broadcaster.Destination;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FilterEventBroadcasterTargetTest extends AbstractBroadcasterTargetTest {

    @Override
    protected BroadcasterTarget createBroadcasterTarget(Destination destination) {
        return new FilterEventBroadcasterTarget(destination, UUID.randomUUID());
    }

    @Test
    void testNullFilterId() {
        assertThrows(
                NullPointerException.class,
                () -> new FilterEventBroadcasterTarget(new Destination("test"), null));
    }
}
