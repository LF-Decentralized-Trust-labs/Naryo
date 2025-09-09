package io.naryo.domain.broadcaster.target;

import java.util.List;
import java.util.UUID;

import io.naryo.domain.broadcaster.AbstractBroadcasterTargetTest;
import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.common.Destination;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FilterEventBroadcasterTargetTest extends AbstractBroadcasterTargetTest {

    @Override
    protected BroadcasterTarget createBroadcasterTarget(List<Destination> destinations) {
        return new FilterEventBroadcasterTarget(destinations, UUID.randomUUID());
    }

    @Test
    void testNullFilterId() {
        assertThrows(
                NullPointerException.class,
                () -> new FilterEventBroadcasterTarget(List.of(new Destination("test")), null));
    }
}
