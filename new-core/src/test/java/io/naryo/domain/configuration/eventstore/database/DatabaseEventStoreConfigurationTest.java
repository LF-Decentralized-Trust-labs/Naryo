package io.naryo.domain.configuration.eventstore.database;

import io.naryo.domain.configuration.eventstore.EventStoreConfiguration;
import io.naryo.domain.configuration.eventstore.EventStoreType;
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
        protected MockDatabaseEventStoreConfiguration(DatabaseEngine databaseEngine) {
            super(databaseEngine);
        }

        @Override
        public EventStoreConfiguration merge(EventStoreConfiguration other) {
            return this;
        }
    }
}
