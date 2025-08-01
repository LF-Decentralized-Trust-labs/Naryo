package io.naryo.infrastructure.configuration.persistence.entity.node.connection.http;

import io.naryo.application.configuration.source.model.node.connection.HttpNodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.endpoint.ConnectionEndpointDescriptor;
import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import io.naryo.infrastructure.configuration.persistence.entity.common.ConnectionEndpointEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.connection.ConnectionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.connection.NodeConnectionRetryEntity;
import jakarta.annotation.Nullable;
import lombok.Setter;

import java.time.Duration;
import java.util.Optional;

public class HttpConnectionEntity extends ConnectionEntity implements HttpNodeConnectionDescriptor {

    private static final int DEFAULT_MAX_IDLE_CONNECTIONS = 5;
    private static final Duration DEFAULT_KEEP_ALIVE_DURATION = Duration.ofMinutes(5);
    private static final Duration DEFAULT_CONNECTION_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration DEFAULT_READ_TIMEOUT = Duration.ofSeconds(30);

    private @Setter
    @Nullable Integer maxIdleConnections;
    private @Setter @Nullable Duration keepAliveDuration;
    private @Setter @Nullable Duration connectionTimeout;
    private @Setter @Nullable Duration readTimeout;

    public HttpConnectionEntity(
        NodeConnectionRetryEntity retry,
        ConnectionEndpointEntity endpoint,
        @Nullable Integer maxIdleConnections,
        @Nullable Duration keepAliveDuration,
        @Nullable Duration connectionTimeout,
        @Nullable Duration readTimeout) {
        super(retry, endpoint);
        this.maxIdleConnections = maxIdleConnections != null ? maxIdleConnections : DEFAULT_MAX_IDLE_CONNECTIONS;
        this.keepAliveDuration = keepAliveDuration != null ? keepAliveDuration : DEFAULT_KEEP_ALIVE_DURATION;
        this.connectionTimeout = connectionTimeout != null ? connectionTimeout : DEFAULT_CONNECTION_TIMEOUT;
        this.readTimeout = readTimeout != null ? readTimeout : DEFAULT_READ_TIMEOUT;
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
