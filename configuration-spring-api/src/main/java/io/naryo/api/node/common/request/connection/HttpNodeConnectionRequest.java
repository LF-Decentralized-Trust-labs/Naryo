package io.naryo.api.node.common.request.connection;

import java.time.Duration;

import io.naryo.application.configuration.source.model.node.connection.factory.DefaultNodeConnectionFactory;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.connection.http.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Schema(description = "HTTP node connection request")
@Getter
public final class HttpNodeConnectionRequest extends NodeConnectionRequest {

    @Positive
    @Schema(defaultValue = "5")
    private final Integer maxIdleConnections;

    @Schema(defaultValue = "PT5M", description = "ISO 8601 duration, must be positive")
    private final Duration keepAliveDuration;

    @Schema(defaultValue = "PT10S", description = "ISO 8601 duration, must be positive")
    private final Duration connectionTimeout;

    @Schema(defaultValue = "PT30S", description = "ISO 8601 duration, must be positive")
    private final Duration readTimeout;

    public HttpNodeConnectionRequest(
            ConnectionEndpointRequest connectionEndpoint,
            RetryConfigurationRequest retryConfiguration,
            Integer maxIdleConnections,
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
        RetryConfigurationRequest retry =
                this.retryConfiguration != null
                        ? this.retryConfiguration
                        : new RetryConfigurationRequest(null, null);
        return new HttpNodeConnection(
                this.connectionEndpoint.toDomain(),
                retry.toDomain(),
                new MaxIdleConnections(
                        maxIdleConnections != null
                                ? maxIdleConnections
                                : DefaultNodeConnectionFactory.DEFAULT_HTTP_MAX_IDLE_CONNECTIONS),
                new KeepAliveDuration(
                        keepAliveDuration != null
                                ? keepAliveDuration
                                : DefaultNodeConnectionFactory.DEFAULT_HTTP_KEEP_ALIVE_DURATION),
                new ConnectionTimeout(
                        connectionTimeout != null
                                ? connectionTimeout
                                : DefaultNodeConnectionFactory.DEFAULT_HTTP_CONNECTION_TIMEOUT),
                new ReadTimeout(
                        readTimeout != null
                                ? readTimeout
                                : DefaultNodeConnectionFactory.DEFAULT_HTTP_READ_TIMEOUT));
    }
}
