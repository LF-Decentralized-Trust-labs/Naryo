package io.naryo.domain.broadcaster.target;

import java.util.UUID;

import io.naryo.domain.broadcaster.AbstractBroadcasterTargetTest;
import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.Destination;
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
