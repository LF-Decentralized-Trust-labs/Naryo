package io.naryo.api.node.common.response.connection;

import java.time.Duration;

import lombok.Builder;

@Builder
public record HttpNodeConnectionResponse(
        String type,
        RetryConfigurationResponse retryConfiguration,
        ConnectionEndpointResponse connectionEndpoint,
        int maxIdleConnections,
        Duration keepAliveDuration,
        Duration connectionTimeout,
        Duration readTimeout)
        implements NodeConnectionResponse {}
