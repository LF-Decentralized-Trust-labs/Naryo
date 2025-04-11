package io.librevents.domain.node.connection.http;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeepAliveDurationTest {

    @Test
    void testValidKeepAliveDuration() {
        KeepAliveDuration duration = new KeepAliveDuration(Duration.ofSeconds(30));
        assertEquals(Duration.ofSeconds(30), duration.value());
    }

    @Test
    void testNegativeKeepAliveDuration() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new KeepAliveDuration(Duration.ofSeconds(-1)));
        assertEquals("Must be positive", exception.getMessage());
    }

    @Test
    void testNullKeepAliveDuration() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new KeepAliveDuration(null));
        assertEquals("Must be positive", exception.getMessage());
    }
}
