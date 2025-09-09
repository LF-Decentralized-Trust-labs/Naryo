package io.naryo.domain.broadcaster;

import java.util.List;

import io.naryo.domain.common.Destination;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractBroadcasterTargetTest {

    protected abstract BroadcasterTarget createBroadcasterTarget(List<Destination> destinations);

    @Test
    void testNullDestination() {
        assertThrows(NullPointerException.class, () -> createBroadcasterTarget(null));
    }

    @Test
    void testValidDestination() {
        Destination destination = new Destination("test");
        BroadcasterTarget target = createBroadcasterTarget(List.of(destination));
        assertNotNull(target);
        assertEquals(List.of(destination), target.getDestinations());
    }
}
