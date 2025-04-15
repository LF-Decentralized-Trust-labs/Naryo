package io.librevents.domain.eventstore.database;

import io.librevents.domain.eventstore.EventStoreConfiguration;
import io.librevents.domain.eventstore.EventStoreType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class DatabaseEventStoreConfiguration extends EventStoreConfiguration {

    private final DatabaseEngine engine;

    protected DatabaseEventStoreConfiguration(DatabaseEngine engine) {
        super(EventStoreType.DATABASE);
        this.engine = engine;
    }
}
