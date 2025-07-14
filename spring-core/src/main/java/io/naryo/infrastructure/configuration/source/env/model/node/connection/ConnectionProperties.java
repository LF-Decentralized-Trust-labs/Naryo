package io.naryo.infrastructure.configuration.source.env.model.node.connection;

import io.naryo.domain.node.connection.NodeConnectionType;
import io.naryo.infrastructure.configuration.source.env.model.common.ConnectionEndpointProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public abstract class ConnectionProperties {

    private final @Getter @NotNull NodeConnectionType type;
    private @Getter @Setter @Valid @NotNull NodeConnectionRetryProperties retry;
    private @Getter @Setter @Valid @NotNull ConnectionEndpointProperties endpoint;

    public ConnectionProperties(
            NodeConnectionType type,
            NodeConnectionRetryProperties retry,
            ConnectionEndpointProperties endpoint) {
        this.type = type;
        this.retry = retry;
        this.endpoint = endpoint;
    }
}
