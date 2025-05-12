package io.librevents.domain.broadcaster.configuration;

import java.time.Duration;

import io.librevents.domain.broadcaster.BroadcasterType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BroadcasterConfigurationTest {

    private static class MockBroadcasterType implements BroadcasterType {
        @Override
        public String getName() {
            return "mock";
        }
    }

    private static class MockBroadcasterConfiguration extends BroadcasterConfiguration {
        protected MockBroadcasterConfiguration(BroadcasterType type, BroadcasterCache cache) {
            super(type, cache);
        }
    }

    @Test
    void testValidConfiguration() {
        MockBroadcasterConfiguration config =
                new MockBroadcasterConfiguration(
                        new MockBroadcasterType(), new BroadcasterCache(Duration.ofSeconds(1)));
        assertEquals("mock", config.getType().getName());
        assertEquals(Duration.ofSeconds(1), config.getCache().expirationTime());
    }

    @Test
    void testNullType() {
        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () ->
                                new MockBroadcasterConfiguration(
                                        null, new BroadcasterCache(Duration.ofSeconds(1))));
        assertEquals("type must not be null", exception.getMessage());
    }

    @Test
    void testNullCache() {
        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () -> new MockBroadcasterConfiguration(new MockBroadcasterType(), null));
        assertEquals("cache must not be null", exception.getMessage());
    }
}
