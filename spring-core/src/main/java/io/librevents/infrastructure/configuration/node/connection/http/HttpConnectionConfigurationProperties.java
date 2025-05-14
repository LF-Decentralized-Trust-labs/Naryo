package io.librevents.infrastructure.configuration.node.connection.http;

import java.time.Duration;

import io.librevents.infrastructure.configuration.node.connection.ConnectionConfigurationProperties;

public record HttpConnectionConfigurationProperties(
        int maxIdleConnections,
        Duration keepAliveDuration,
        Duration connectionTimeout,
        Duration readTimeout)
        implements ConnectionConfigurationProperties {}
