package io.librevents.domain.node.connection;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RetryConfigurationTest {

    @Test
    void testValidRetryConfiguration() {
        RetryConfiguration config = new RetryConfiguration(3, Duration.ofSeconds(5));
        assertEquals(3, config.times());
        assertEquals(Duration.ofSeconds(5), config.backoff());
    }

    @Test
    void testInvalidRetryTimes() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new RetryConfiguration(-1, Duration.ofSeconds(5)));
        assertEquals("Retry times must be >= 0", exception.getMessage());
    }

    @Test
    void testInvalidBackoffDuration() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new RetryConfiguration(3, Duration.ofSeconds(-1)));
        assertEquals("Backoff duration must be > 0", exception.getMessage());
    }

    @Test
    void testInvalidBackoffDurationZero() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new RetryConfiguration(3, Duration.ZERO));
        assertEquals("Backoff duration must be > 0", exception.getMessage());
    }

    @Test
    void testNullBackoffDuration() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new RetryConfiguration(3, null));
        assertEquals("Backoff duration must be > 0", exception.getMessage());
    }
}
