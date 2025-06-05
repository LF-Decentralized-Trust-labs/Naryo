package io.librevents.infrastructure.configuration.source.env.model.http;

import java.time.Duration;

import jakarta.validation.constraints.NotNull;

public record HttpClientProperties(
        int maxIdleConnections,
        @NotNull Duration keepAliveDuration,
        @NotNull Duration connectTimeout,
        @NotNull Duration readTimeout,
        @NotNull Duration writeTimeout,
        @NotNull Duration callTimeout,
        @NotNull Duration pingInterval,
        boolean retryOnConnectionFailure) {
    private static final int DEFAULT_MAX_IDLE_CONNECTIONS = 5;
    private static final Duration DEFAULT_KEEP_ALIVE_DURATION = Duration.ofMinutes(5);
    private static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration DEFAULT_READ_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration DEFAULT_WRITE_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration DEFAULT_CALL_TIMEOUT = Duration.ofSeconds(60);
    private static final Duration DEFAULT_PING_INTERVAL = Duration.ofSeconds(15);
    private static final boolean DEFAULT_RETRY_ON_CONNECTION_FAILURE = true;

    public HttpClientProperties() {
        this(
                DEFAULT_MAX_IDLE_CONNECTIONS,
                DEFAULT_KEEP_ALIVE_DURATION,
                DEFAULT_CONNECT_TIMEOUT,
                DEFAULT_READ_TIMEOUT,
                DEFAULT_WRITE_TIMEOUT,
                DEFAULT_CALL_TIMEOUT,
                DEFAULT_PING_INTERVAL,
                DEFAULT_RETRY_ON_CONNECTION_FAILURE);
    }

    public HttpClientProperties(
            int maxIdleConnections,
            Duration keepAliveDuration,
            Duration connectTimeout,
            Duration readTimeout,
            Duration writeTimeout,
            Duration callTimeout,
            Duration pingInterval,
            boolean retryOnConnectionFailure) {
        this.maxIdleConnections = maxIdleConnections;
        this.retryOnConnectionFailure = retryOnConnectionFailure;

        this.keepAliveDuration = applyDefaultIfNull(keepAliveDuration, DEFAULT_KEEP_ALIVE_DURATION);
        this.connectTimeout = applyDefaultIfNull(connectTimeout, DEFAULT_CONNECT_TIMEOUT);
        this.readTimeout = applyDefaultIfNull(readTimeout, DEFAULT_READ_TIMEOUT);
        this.writeTimeout = applyDefaultIfNull(writeTimeout, DEFAULT_WRITE_TIMEOUT);
        this.callTimeout = applyDefaultIfNull(callTimeout, DEFAULT_CALL_TIMEOUT);
        this.pingInterval = applyDefaultIfNull(pingInterval, DEFAULT_PING_INTERVAL);
    }

    private Duration applyDefaultIfNull(Duration duration, Duration defaultValue) {
        return (duration == null || duration.isNegative() || duration.isZero())
                ? defaultValue
                : duration;
    }
}
