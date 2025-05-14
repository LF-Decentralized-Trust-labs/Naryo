package io.librevents.infrastructure.configuration.source.env.model.http;

import java.time.Duration;

public record HttpClientProperties(
        int maxIdleConnections,
        Duration keepAliveDuration,
        Duration connectTimeout,
        Duration readTimeout,
        Duration writeTimeout,
        Duration callTimeout,
        Duration pingInterval,
        boolean isRetryOnConnectionFailure) {}
