package io.naryo.domain.configuration.eventstore.server;

import java.util.Set;

import io.naryo.domain.configuration.eventstore.EventStoreType;
import io.naryo.domain.configuration.eventstore.block.server.ServerBlockEventStoreConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerBlockEventStoreConfigurationTest {

    @Test
    void testConstructor() {
        ServerBlockEventStoreConfiguration config =
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

    private static class MockServerEventStoreConfiguration
            extends ServerBlockEventStoreConfiguration {
        protected MockServerEventStoreConfiguration(ServerType serverType) {
            super(Set.of(), serverType);
        }
    }
}
