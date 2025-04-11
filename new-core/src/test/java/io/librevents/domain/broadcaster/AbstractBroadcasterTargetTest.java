package io.librevents.domain.broadcaster;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractBroadcasterTargetTest {

    protected abstract BroadcasterTarget createBroadcasterTarget(Destination destination);

    @Test
    void testNullDestination() {
        assertThrows(NullPointerException.class, () -> createBroadcasterTarget(null));
    }

    @Test
    void testValidDestination() {
        Destination destination = new Destination("test");
        BroadcasterTarget target = createBroadcasterTarget(destination);
        assertNotNull(target);
        assertEquals(destination, target.getDestination());
    }

}
