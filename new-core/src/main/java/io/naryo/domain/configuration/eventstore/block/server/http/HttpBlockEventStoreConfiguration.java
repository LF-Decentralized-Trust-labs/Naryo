package io.naryo.domain.configuration.eventstore.block.server.http;

import java.util.Set;
import java.util.UUID;

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
            UUID nodeId,
            Set<EventStoreTarget> targets,
            ServerType serverType,
            ConnectionEndpoint endpoint) {
        super(nodeId, targets, serverType);
        this.endpoint = endpoint;
    }
}
