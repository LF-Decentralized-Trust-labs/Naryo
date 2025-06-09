package io.naryo.infrastructure.configuration.source.env.model.node.connection.http;

import java.time.Duration;

import io.naryo.infrastructure.configuration.source.env.model.node.connection.ConnectionConfigurationProperties;
import jakarta.validation.constraints.NotNull;

public record HttpConnectionConfigurationProperties(
        int maxIdleConnections,
        @NotNull Duration keepAliveDuration,
        @NotNull Duration connectionTimeout,
        @NotNull Duration readTimeout)
        implements ConnectionConfigurationProperties {

    private static final int DEFAULT_MAX_IDLE_CONNECTIONS = 5;
    private static final Duration DEFAULT_KEEP_ALIVE_DURATION = Duration.ofMinutes(5);
    private static final Duration DEFAULT_CONNECTION_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration DEFAULT_READ_TIMEOUT = Duration.ofSeconds(30);

    public HttpConnectionConfigurationProperties {
        keepAliveDuration =
                keepAliveDuration != null ? keepAliveDuration : DEFAULT_KEEP_ALIVE_DURATION;
        connectionTimeout =
                connectionTimeout != null ? connectionTimeout : DEFAULT_CONNECTION_TIMEOUT;
        readTimeout = readTimeout != null ? readTimeout : DEFAULT_READ_TIMEOUT;
    }

    public HttpConnectionConfigurationProperties() {
        this(
                DEFAULT_MAX_IDLE_CONNECTIONS,
                DEFAULT_KEEP_ALIVE_DURATION,
                DEFAULT_CONNECTION_TIMEOUT,
                DEFAULT_READ_TIMEOUT);
    }
}
