package io.librevents.domain.node.connection.http;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionTimeoutTest {

    @Test
    void testValidTimeout() {
        ConnectionTimeout timeout = new ConnectionTimeout(Duration.ofSeconds(5));
        assertEquals(Duration.ofSeconds(5), timeout.value());
    }

    @Test
    void testZeroTimeout() {
        Exception exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> {
                            new ConnectionTimeout(Duration.ofSeconds(0));
                        });
        assertEquals("Must be > 0", exception.getMessage());
    }

    @Test
    void testNegativeTimeout() {
        Exception exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> {
                            new ConnectionTimeout(Duration.ofSeconds(-1));
                        });
        assertEquals("Must be > 0", exception.getMessage());
    }

    @Test
    void testNullTimeout() {
        Exception exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> {
                            new ConnectionTimeout(null);
                        });
        assertEquals("Must be > 0", exception.getMessage());
    }
}
