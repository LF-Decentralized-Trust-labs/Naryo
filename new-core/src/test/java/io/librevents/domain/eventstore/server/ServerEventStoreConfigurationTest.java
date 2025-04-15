package io.librevents.domain.eventstore.server;

import io.librevents.domain.eventstore.EventStoreType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerEventStoreConfigurationTest {

    private static class MockServerType implements ServerType {
        @Override
        public String getName() {
            return "MockServer";
        }
    }

    private static class MockServerEventStoreConfiguration extends ServerEventStoreConfiguration {
        protected MockServerEventStoreConfiguration(ServerType serverType) {
            super(serverType);
        }
    }

    @Test
    void testConstructor() {
        ServerEventStoreConfiguration config =
                new MockServerEventStoreConfiguration(new MockServerType());
        assertEquals(EventStoreType.SERVER, config.getEventStoreType());
        assertEquals("MockServer", config.getServerType().getName());
    }
}
