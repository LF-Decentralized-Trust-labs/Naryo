package io.naryo.api.node.getAll.model.connection;

import java.time.Duration;

import io.naryo.domain.node.connection.http.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "HTTP node connection")
@Getter
public final class HttpNodeConnectionResponse extends NodeConnectionResponse {

    private final Integer maxIdleConnections;
    private final Duration keepAliveDuration;
    private final Duration connectionTimeout;
    private final Duration readTimeout;

    public HttpNodeConnectionResponse(
            ConnectionEndpointResponse connectionEndpoint,
            RetryConfigurationResponse retryConfiguration,
            int maxIdleConnections,
            Duration keepAliveDuration,
            Duration connectionTimeout,
            Duration readTimeout) {
        super(connectionEndpoint, retryConfiguration);
        this.maxIdleConnections = maxIdleConnections;
        this.keepAliveDuration = keepAliveDuration;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
    }

    public static HttpNodeConnectionResponse fromDomain(HttpNodeConnection httpNodeConnection) {
        return new HttpNodeConnectionResponse(
                ConnectionEndpointResponse.fromDomain(httpNodeConnection.getEndpoint()),
                RetryConfigurationResponse.fromDomain(httpNodeConnection.getRetryConfiguration()),
                httpNodeConnection.getMaxIdleConnections().value(),
                httpNodeConnection.getKeepAliveDuration().value(),
                httpNodeConnection.getConnectionTimeout().value(),
                httpNodeConnection.getReadTimeout().value());
    }
}
