package io.naryo.api.node.common.request.connection;

import java.time.Duration;

import io.naryo.domain.node.connection.NodeConnectionType;
import lombok.Getter;

@Getter
public final class HttpNodeConnectionRequest extends NodeConnectionRequest {

    private final int maxIdleConnections;
    private final Duration keepAliveDuration;
    private final Duration connectionTimeout;
    private final Duration readTimeout;

    public HttpNodeConnectionRequest(
            RetryConfigurationRequest retryConfiguration,
            ConnectionEndpointRequest connectionEndpoint,
            int maxIdleConnections,
            Duration keepAliveDuration,
            Duration connectionTimeout,
            Duration readTimeout) {
        super(NodeConnectionType.HTTP, retryConfiguration, connectionEndpoint);
        this.maxIdleConnections = maxIdleConnections;
        this.keepAliveDuration = keepAliveDuration;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
    }
}
