package io.librevents.domain.eventstore.database;

import io.librevents.domain.eventstore.EventStoreType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseEventStoreConfigurationTest {

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
    }

    @Test
    void testConstructor() {
        DatabaseEventStoreConfiguration config =
                new MockDatabaseEventStoreConfiguration(new MockDatabaseEngine());
        assertEquals(EventStoreType.DATABASE, config.getType());
        assertEquals("MockDatabase", config.getEngine().getName());
    }
}
