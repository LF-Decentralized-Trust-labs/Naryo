package io.librevents.domain.node.subscription.block.method.poll;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class IntervalTest {

    @Test
    void testIntervalCreation() {
        Interval interval = new Interval(Duration.ofSeconds(10));
        assertEquals(Duration.ofSeconds(10), interval.value());
    }

    @Test
    void testIntervalCreationWithNegativeValue() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class, () -> new Interval(Duration.ofSeconds(-1)));
        assertEquals("Must be positive", exception.getMessage());
    }

    @Test
    void testIntervalCreationWithNullValue() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new Interval(null));
        assertEquals("Must be positive", exception.getMessage());
    }
}
