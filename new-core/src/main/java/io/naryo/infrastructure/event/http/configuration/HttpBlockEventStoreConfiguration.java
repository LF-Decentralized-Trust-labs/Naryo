package io.naryo.infrastructure.event.http.configuration;

import java.util.Set;

import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.configuration.eventstore.block.EventStoreTarget;
import io.naryo.domain.configuration.eventstore.block.server.ServerBlockEventStoreConfiguration;
import io.naryo.domain.configuration.eventstore.server.ServerType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class HttpBlockEventStoreConfiguration extends ServerBlockEventStoreConfiguration {

    private final ConnectionEndpoint endpoint;

    public HttpBlockEventStoreConfiguration(
            Set<EventStoreTarget> targets, ServerType serverType, ConnectionEndpoint endpoint) {
        super(targets, serverType);
        this.endpoint = endpoint;
    }
}
