package io.librevents.domain.broadcaster;

import java.time.Duration;

import io.librevents.domain.broadcaster.configuration.BroadcasterCache;
import io.librevents.domain.broadcaster.configuration.BroadcasterConfiguration;
import io.librevents.domain.broadcaster.target.AllBroadcasterTarget;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BroadcasterTest {

    private static class MockBroadcasterType implements BroadcasterType {
        @Override
        public String getName() {
            return "test";
        }
    }

    private static class MockBroadcasterConfiguration extends BroadcasterConfiguration {
        protected MockBroadcasterConfiguration() {
            super(new MockBroadcasterType(), new BroadcasterCache(Duration.ofSeconds(1)));
        }
    }

    @Test
    void testBroadcasterCreation() {
        AllBroadcasterTarget target = new AllBroadcasterTarget(new Destination("value"));
        BroadcasterConfiguration configuration = new MockBroadcasterConfiguration();
        Broadcaster broadcaster = new Broadcaster(target, configuration);

        assertEquals(target, broadcaster.target());
        assertEquals(configuration, broadcaster.configuration());
        assertEquals("test", broadcaster.configuration().getType().getName());
        assertEquals(
                Duration.ofSeconds(1), broadcaster.configuration().getCache().expirationTime());
    }
}
