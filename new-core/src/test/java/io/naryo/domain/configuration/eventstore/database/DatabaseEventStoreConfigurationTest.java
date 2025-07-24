package io.naryo.domain.configuration.eventstore.database;

import java.util.Set;

import io.naryo.domain.configuration.eventstore.EventStoreType;
import io.naryo.domain.configuration.eventstore.block.database.DatabaseEventStoreConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseEventStoreConfigurationTest {

    @Test
    void testConstructor() {
        DatabaseEventStoreConfiguration config =
                new MockDatabaseEventStoreConfiguration(new MockDatabaseEngine());
        assertEquals(EventStoreType.DATABASE, config.getType());
        assertEquals("MockDatabase", config.getEngine().getName());
    }

    private static class MockDatabaseEngine implements DatabaseEngine {
        @Override
        public String getName() {
            return "MockDatabase";
        }
    }

    private static class MockDatabaseEventStoreConfiguration
            extends DatabaseEventStoreConfiguration {

        protected MockDatabaseEventStoreConfiguration(DatabaseEngine engine) {
            super(engine, Set.of());
        }
    }
}
