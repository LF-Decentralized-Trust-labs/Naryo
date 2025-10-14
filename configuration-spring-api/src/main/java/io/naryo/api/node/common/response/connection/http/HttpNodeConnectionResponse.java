package io.naryo.api.node.common.response.connection.http;

import java.time.Duration;

import io.naryo.api.node.common.response.connection.ConnectionEndpointResponse;
import io.naryo.api.node.common.response.connection.NodeConnectionResponse;
import io.naryo.api.node.common.response.connection.RetryConfigurationResponse;

public final class HttpNodeConnectionResponse extends NodeConnectionResponse {

    private final int maxIdleConnections;
    private final Duration keepAliveDuration;
    private final Duration connectionTimeout;
    private final Duration readTimeout;

    public HttpNodeConnectionResponse(
            String type,
            RetryConfigurationResponse retryConfiguration,
            ConnectionEndpointResponse connectionEndpoint,
            int maxIdleConnections,
            Duration keepAliveDuration,
            Duration connectionTimeout,
            Duration readTimeout) {
        super(type, retryConfiguration, connectionEndpoint);
        this.maxIdleConnections = maxIdleConnections;
        this.keepAliveDuration = keepAliveDuration;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
    }
}
