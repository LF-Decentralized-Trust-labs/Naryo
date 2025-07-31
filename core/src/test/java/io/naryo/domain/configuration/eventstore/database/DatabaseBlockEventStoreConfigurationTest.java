package io.naryo.domain.configuration.eventstore.database;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.broadcaster.Destination;
import io.naryo.domain.configuration.eventstore.EventStoreType;
import io.naryo.domain.configuration.eventstore.block.EventStoreTarget;
import io.naryo.domain.configuration.eventstore.block.TargetType;
import io.naryo.domain.configuration.eventstore.block.database.DatabaseBlockEventStoreConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseBlockEventStoreConfigurationTest {

    @Test
    void testConstructor() {
        DatabaseBlockEventStoreConfiguration config =
                new MockDatabaseBlockEventStoreConfiguration(new MockDatabaseEngine());
        assertEquals(EventStoreType.DATABASE, config.getType());
        assertEquals("MockDatabase", config.getEngine().getName());
    }

    private static class MockDatabaseEngine implements DatabaseEngine {
        @Override
        public String getName() {
            return "MockDatabase";
        }
    }

    private static class MockDatabaseBlockEventStoreConfiguration
            extends DatabaseBlockEventStoreConfiguration {

        protected MockDatabaseBlockEventStoreConfiguration(DatabaseEngine engine) {
            super(
                    UUID.randomUUID(),
                    engine,
                    Set.of(
                            new EventStoreTarget(TargetType.BLOCK, new Destination("xyc")),
                            new EventStoreTarget(TargetType.TRANSACTION, new Destination("xyc")),
                            new EventStoreTarget(
                                    TargetType.CONTRACT_EVENT, new Destination("xyc"))));
        }
    }
}
