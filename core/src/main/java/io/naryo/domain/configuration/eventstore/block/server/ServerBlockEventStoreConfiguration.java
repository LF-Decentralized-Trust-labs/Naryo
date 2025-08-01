package io.naryo.domain.configuration.eventstore.block.server;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.configuration.eventstore.BlockEventStoreConfiguration;
import io.naryo.domain.configuration.eventstore.EventStoreType;
import io.naryo.domain.configuration.eventstore.block.EventStoreTarget;
import io.naryo.domain.configuration.eventstore.server.ServerType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class ServerBlockEventStoreConfiguration extends BlockEventStoreConfiguration {

    protected final ServerType serverType;

    protected ServerBlockEventStoreConfiguration(
            UUID nodeId, Set<EventStoreTarget> targets, ServerType serverType) {
        super(nodeId, EventStoreType.SERVER, targets);
        this.serverType = serverType;
    }
}
