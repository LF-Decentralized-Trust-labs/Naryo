package io.naryo.domain.broadcaster;

import java.util.UUID;

import io.naryo.domain.broadcaster.target.AllBroadcasterTarget;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BroadcasterTest {

    private static class MockBroadcasterType implements BroadcasterType {
        @Override
        public String getName() {
            return "test";
        }
    }

    @Test
    void testBroadcasterCreation() {
        AllBroadcasterTarget target = new AllBroadcasterTarget(new Destination("value"));
        UUID configurationId = UUID.randomUUID();
        Broadcaster broadcaster = new Broadcaster(UUID.randomUUID(), target, configurationId);

        assertEquals(target, broadcaster.getTarget());
        assertEquals(configurationId, broadcaster.getConfigurationId());
    }
}
