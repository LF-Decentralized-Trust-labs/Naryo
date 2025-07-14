package io.naryo.domain.configuration.eventstore.server;

import io.naryo.domain.configuration.eventstore.EventStoreType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerEventStoreConfigurationTest {

    @Test
    void testConstructor() {
        ServerEventStoreConfiguration config =
                new MockServerEventStoreConfiguration(new MockServerType());
        assertEquals(EventStoreType.SERVER, config.getType());
        assertEquals("MockServer", config.getServerType().getName());
    }

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
}
