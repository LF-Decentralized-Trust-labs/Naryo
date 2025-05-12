package io.librevents.domain.node.connection.http;

import java.util.Objects;

import io.librevents.domain.common.connection.endpoint.ConnectionEndpoint;
import io.librevents.domain.common.connection.endpoint.Protocol;
import io.librevents.domain.node.connection.NodeConnection;
import io.librevents.domain.node.connection.NodeConnectionType;
import io.librevents.domain.node.connection.RetryConfiguration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class HttpNodeConnection extends NodeConnection {

    private final MaxIdleConnections maxIdleConnections;
    private final KeepAliveDuration keepAliveDuration;
    private final ConnectionTimeout connectionTimeout;
    private final ReadTimeout readTimeout;

    public HttpNodeConnection(
            ConnectionEndpoint endpoint,
            RetryConfiguration retryConfiguration,
            MaxIdleConnections maxIdleConnections,
            KeepAliveDuration keepAliveDuration,
            ConnectionTimeout connectionTimeout,
            ReadTimeout readTimeout) {
        super(NodeConnectionType.HTTP, endpoint, retryConfiguration);
        Objects.requireNonNull(maxIdleConnections, "maxIdleConnections cannot be null");
        Objects.requireNonNull(keepAliveDuration, "keepAliveDuration cannot be null");
        Objects.requireNonNull(connectionTimeout, "connectionTimeout cannot be null");
        Objects.requireNonNull(readTimeout, "readTimeout cannot be null");
        this.maxIdleConnections = maxIdleConnections;
        this.keepAliveDuration = keepAliveDuration;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;

        if (!(endpoint.getProtocol() == Protocol.HTTP
                || endpoint.getProtocol() == Protocol.HTTPS)) {
            throw new IllegalArgumentException(
                    "Invalid protocol for HTTP connection: " + endpoint.getProtocol());
        }
    }
}
