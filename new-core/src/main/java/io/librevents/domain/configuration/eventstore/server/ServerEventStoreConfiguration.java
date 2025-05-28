package io.librevents.domain.configuration.eventstore.server;

import io.librevents.domain.configuration.eventstore.EventStoreConfiguration;
import io.librevents.domain.configuration.eventstore.EventStoreType;
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
