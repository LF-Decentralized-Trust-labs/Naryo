package io.librevents.infrastructure.configuration.node.connection;

import java.time.Duration;

public record RetryConfigurationProperties(int times, Duration backoff) {}
