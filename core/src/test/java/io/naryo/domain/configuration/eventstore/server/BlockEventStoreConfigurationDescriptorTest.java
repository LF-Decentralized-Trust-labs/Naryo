package io.naryo.domain.configuration.eventstore.server;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.broadcaster.Destination;
import io.naryo.domain.configuration.eventstore.active.EventStoreType;
import io.naryo.domain.configuration.eventstore.active.block.BlockEventStoreConfiguration;
import io.naryo.domain.configuration.eventstore.active.block.EventStoreTarget;
import io.naryo.domain.configuration.eventstore.active.block.TargetType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BlockEventStoreConfigurationDescriptorTest {

    @Test
    void testConstructor() {
        BlockEventStoreConfiguration config =
                new MockServerEventStoreConfiguration(new MockServerType());
        assertEquals("MockType", config.getType().getName());
    }

    private static class MockServerType implements EventStoreType {
        @Override
        public String getName() {
            return "MockType";
        }
    }

    private static class MockServerEventStoreConfiguration extends BlockEventStoreConfiguration {
        protected MockServerEventStoreConfiguration(EventStoreType type) {
            super(
                    UUID.randomUUID(),
                    type,
                    Set.of(
                            new EventStoreTarget(TargetType.BLOCK, new Destination("xyc")),
                            new EventStoreTarget(TargetType.TRANSACTION, new Destination("xyc")),
                            new EventStoreTarget(
                                    TargetType.CONTRACT_EVENT, new Destination("xyc"))));
        }
    }
}
