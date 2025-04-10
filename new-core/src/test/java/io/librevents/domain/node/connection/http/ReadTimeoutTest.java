package io.librevents.domain.node.connection.http;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReadTimeoutTest {

    @Test
    void testValidReadTimeout() {
        ReadTimeout readTimeout = new ReadTimeout(Duration.ofSeconds(30));
        assertEquals(Duration.ofSeconds(30), readTimeout.value());
    }

    @Test
    void testNegativeReadTimeout() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new ReadTimeout(Duration.ofSeconds(-1)));
        assertEquals("Must be > 0", exception.getMessage());
    }

    @Test
    void testZeroReadTimeout() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new ReadTimeout(Duration.ofSeconds(0)));
        assertEquals("Must be > 0", exception.getMessage());
    }

    @Test
    void testNullReadTimeout() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new ReadTimeout(null));
        assertEquals("Must be > 0", exception.getMessage());
    }
}
