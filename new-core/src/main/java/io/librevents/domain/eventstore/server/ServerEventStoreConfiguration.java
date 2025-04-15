package io.librevents.domain.eventstore.server;

import io.librevents.domain.eventstore.EventStoreConfiguration;
import io.librevents.domain.eventstore.EventStoreType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class ServerEventStoreConfiguration extends EventStoreConfiguration {

    private final ServerType serverType;

    protected ServerEventStoreConfiguration(ServerType serverType) {
        super(EventStoreType.SERVER);
        this.serverType = serverType;
    }
}
