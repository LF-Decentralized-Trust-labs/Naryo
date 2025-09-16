package io.naryo.infrastructure.configuration.source.env.model.node.connection.http;

import java.time.Duration;
import java.util.Optional;

import io.naryo.application.configuration.source.model.node.connection.HttpNodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import io.naryo.infrastructure.configuration.source.env.model.common.ConnectionEndpointProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.ConnectionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.NodeConnectionRetryProperties;
import jakarta.annotation.Nullable;
import lombok.Setter;

public final class HttpConnectionProperties extends ConnectionProperties
        implements HttpNodeConnectionDescriptor {

    private @Setter @Nullable Integer maxIdleConnections;
    private @Setter @Nullable Duration keepAliveDuration;
    private @Setter @Nullable Duration connectionTimeout;
    private @Setter @Nullable Duration readTimeout;

    public HttpConnectionProperties(
            NodeConnectionRetryDescriptor retry,
            ConnectionEndpointDescriptor endpoint,
            Integer maxIdleConnections,
            Duration keepAliveDuration,
            Duration connectionTimeout,
            Duration readTimeout) {
        super(retry, endpoint);
        this.maxIdleConnections = maxIdleConnections;
        this.keepAliveDuration = keepAliveDuration;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
    }

    public HttpConnectionProperties(
            NodeConnectionRetryProperties retry, ConnectionEndpointProperties endpoint) {
        super(retry, endpoint);
    }

    @Override
    public Optional<Integer> getMaxIdleConnections() {
        return Optional.ofNullable(maxIdleConnections);
    }

    @Override
    public Optional<Duration> getKeepAliveDuration() {
        return Optional.ofNullable(keepAliveDuration);
    }

    @Override
    public Optional<Duration> getConnectionTimeout() {
        return Optional.ofNullable(connectionTimeout);
    }

    @Override
    public Optional<Duration> getReadTimeout() {
        return Optional.ofNullable(readTimeout);
    }
}
