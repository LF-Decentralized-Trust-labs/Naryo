package io.naryo.domain.configuration.eventstore.server;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.broadcaster.Destination;
import io.naryo.domain.configuration.eventstore.EventStoreType;
import io.naryo.domain.configuration.eventstore.block.EventStoreTarget;
import io.naryo.domain.configuration.eventstore.block.TargetType;
import io.naryo.domain.configuration.eventstore.block.server.ServerBlockEventStoreConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerBlockEventStoreConfigurationDescriptorTest {

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
            super(
                    UUID.randomUUID(),
                    Set.of(
                            new EventStoreTarget(TargetType.BLOCK, new Destination("xyc")),
                            new EventStoreTarget(TargetType.TRANSACTION, new Destination("xyc")),
                            new EventStoreTarget(
                                    TargetType.CONTRACT_EVENT, new Destination("xyc"))),
                    serverType);
        }
    }
}
