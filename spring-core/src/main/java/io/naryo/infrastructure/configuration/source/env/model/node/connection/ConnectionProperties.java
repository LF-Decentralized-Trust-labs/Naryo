package io.naryo.infrastructure.configuration.source.env.model.node.connection;

import java.util.Optional;

import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import io.naryo.domain.node.connection.NodeConnectionType;
import io.naryo.infrastructure.configuration.source.env.model.common.ConnectionEndpointProperties;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;

public abstract class ConnectionProperties implements NodeConnectionDescriptor {

    private final @Getter @NotNull NodeConnectionType type;
    private @Valid @Nullable NodeConnectionRetryDescriptor retry;
    private @Valid @Nullable ConnectionEndpointDescriptor endpoint;

    public ConnectionProperties(
            NodeConnectionType type,
            NodeConnectionRetryDescriptor retry,
            ConnectionEndpointDescriptor endpoint) {
        this.type = type;
        this.retry = retry;
        this.endpoint = endpoint;
    }

    @Override
    public Optional<NodeConnectionRetryDescriptor> getRetry() {
        return Optional.ofNullable(retry);
    }

    @Override
    public void setRetry(NodeConnectionRetryDescriptor retry) {
        this.retry =
                new NodeConnectionRetryProperties(
                        valueOrNull(retry.getTimes()), valueOrNull(retry.getBackoff()));
    }

    @Override
    public Optional<ConnectionEndpointDescriptor> getEndpoint() {
        return Optional.ofNullable(endpoint);
    }

    @Override
    public void setEndpoint(ConnectionEndpointDescriptor endpoint) {
        this.endpoint = new ConnectionEndpointProperties(valueOrNull(endpoint.getUrl()));
    }
}
