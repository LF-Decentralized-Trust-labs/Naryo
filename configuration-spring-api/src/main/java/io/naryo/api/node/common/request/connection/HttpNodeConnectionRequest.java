package io.naryo.api.node.common.request.connection;

import java.time.Duration;

import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.connection.http.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "HTTP node connection")
@Getter
public final class HttpNodeConnectionRequest extends NodeConnectionRequest {

    private final Integer maxIdleConnections;
    private final Duration keepAliveDuration;
    private final Duration connectionTimeout;
    private final Duration readTimeout;

    public HttpNodeConnectionRequest(
            ConnectionEndpointRequest connectionEndpoint,
            RetryConfigurationRequest retryConfiguration,
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

    @Override
    public NodeConnection toDomain() {
        return new HttpNodeConnection(
                this.connectionEndpoint.toDomain(),
                this.retryConfiguration.toDomain(),
                new MaxIdleConnections(this.maxIdleConnections),
                new KeepAliveDuration(this.keepAliveDuration),
                new ConnectionTimeout(this.connectionTimeout),
                new ReadTimeout(this.readTimeout));
    }
}
