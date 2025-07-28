package io.naryo.domain.configuration.eventstore.block.database;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.configuration.eventstore.BlockEventStoreConfiguration;
import io.naryo.domain.configuration.eventstore.EventStoreType;
import io.naryo.domain.configuration.eventstore.block.EventStoreTarget;
import io.naryo.domain.configuration.eventstore.database.DatabaseEngine;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class DatabaseBlockEventStoreConfiguration extends BlockEventStoreConfiguration {

    private final DatabaseEngine engine;

    protected DatabaseBlockEventStoreConfiguration(
            UUID nodeId, DatabaseEngine engine, Set<EventStoreTarget> targets) {
        super(nodeId, EventStoreType.DATABASE, targets);
        this.engine = engine;
    }
}
