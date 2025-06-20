package io.naryo.domain.node.connection;

import java.util.Objects;

import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public abstract class NodeConnection {

    private final NodeConnectionType type;
    protected RetryConfiguration retryConfiguration;
    protected ConnectionEndpoint endpoint;

    protected NodeConnection(
            NodeConnectionType type,
            ConnectionEndpoint endpoint,
            RetryConfiguration retryConfiguration) {
        Objects.requireNonNull(retryConfiguration, "RetryConfiguration cannot be null");
        Objects.requireNonNull(endpoint, "Endpoint cannot be null");
        Objects.requireNonNull(type, "Type cannot be null");

        this.type = type;
        this.endpoint = endpoint;
        this.retryConfiguration = retryConfiguration;
    }
}
