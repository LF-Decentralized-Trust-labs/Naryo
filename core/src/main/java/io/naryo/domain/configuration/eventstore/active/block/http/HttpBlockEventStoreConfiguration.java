package io.naryo.domain.configuration.eventstore.active.block.http;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.configuration.eventstore.active.block.BlockEventStoreConfiguration;
import io.naryo.domain.configuration.eventstore.active.block.EventStoreTarget;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class HttpBlockEventStoreConfiguration extends BlockEventStoreConfiguration {

    private final ConnectionEndpoint endpoint;

    public HttpBlockEventStoreConfiguration(
            UUID nodeId, Set<EventStoreTarget> targets, ConnectionEndpoint endpoint) {
        super(nodeId, () -> "http", targets);
        this.endpoint = endpoint;
    }
}
