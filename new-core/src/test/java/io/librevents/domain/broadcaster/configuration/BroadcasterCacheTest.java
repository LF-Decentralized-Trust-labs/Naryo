package io.librevents.domain.broadcaster.configuration;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BroadcasterCacheTest {

    @Test
    void testValidCacheSize() {
        BroadcasterCache cache = new BroadcasterCache(Duration.ofSeconds(1));
        assertEquals(cache.expirationTime(), Duration.ofSeconds(1));
    }

    @Test
    void testInvalidCacheSize() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new BroadcasterCache(Duration.ofSeconds(0)));
        assertEquals("expirationTime must be positive", exception.getMessage());
    }

    @Test
    void testNullCacheSize() {
        NullPointerException exception =
                assertThrows(NullPointerException.class, () -> new BroadcasterCache(null));
        assertEquals("expirationTime must not be null", exception.getMessage());
    }

    @Test
    void testNegativeCacheSize() {
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new BroadcasterCache(Duration.ofSeconds(-1)));
        assertEquals("expirationTime must be positive", exception.getMessage());
    }
}
