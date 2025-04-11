package io.librevents.domain.filter.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CorrelationIdTest {

    @Test
    void testCorrelationId() {
        CorrelationId correlationId = new CorrelationId(5);
        assertEquals(5, correlationId.position());
    }

    @Test
    void testCorrelationIdNegativePosition() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new CorrelationId(-1));
        assertEquals("position must be greater than or equal to 0", exception.getMessage());
    }
}
