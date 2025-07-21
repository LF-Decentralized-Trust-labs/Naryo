package io.naryo.infrastructure.configuration.source.env.model.node.connection.http;

import java.time.Duration;
import java.util.Optional;

import io.naryo.application.configuration.source.model.node.connection.HttpNodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import io.naryo.domain.node.connection.NodeConnectionType;
import io.naryo.infrastructure.configuration.source.env.model.common.ConnectionEndpointProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.ConnectionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.NodeConnectionRetryProperties;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public final class HttpConnectionProperties extends ConnectionProperties
        implements HttpNodeConnectionDescriptor {

    private static final int DEFAULT_MAX_IDLE_CONNECTIONS = 5;
    private static final Duration DEFAULT_KEEP_ALIVE_DURATION = Duration.ofMinutes(5);
    private static final Duration DEFAULT_CONNECTION_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration DEFAULT_READ_TIMEOUT = Duration.ofSeconds(30);

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
        super(NodeConnectionType.HTTP, retry, endpoint);
        this.maxIdleConnections =
                maxIdleConnections != null ? maxIdleConnections : DEFAULT_MAX_IDLE_CONNECTIONS;
        this.keepAliveDuration =
                keepAliveDuration != null ? keepAliveDuration : DEFAULT_KEEP_ALIVE_DURATION;
        this.connectionTimeout =
                connectionTimeout != null ? connectionTimeout : DEFAULT_CONNECTION_TIMEOUT;
        this.readTimeout = readTimeout != null ? readTimeout : DEFAULT_READ_TIMEOUT;
    }

    public HttpConnectionProperties(
            NodeConnectionRetryProperties retry, ConnectionEndpointProperties endpoint) {
        this(
                retry,
                endpoint,
                DEFAULT_MAX_IDLE_CONNECTIONS,
                DEFAULT_KEEP_ALIVE_DURATION,
                DEFAULT_CONNECTION_TIMEOUT,
                DEFAULT_READ_TIMEOUT);
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
