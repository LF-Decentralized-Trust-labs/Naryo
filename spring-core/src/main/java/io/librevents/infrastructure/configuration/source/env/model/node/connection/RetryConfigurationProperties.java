package io.librevents.infrastructure.configuration.source.env.model.node.connection;

import java.time.Duration;

public record RetryConfigurationProperties(int times, Duration backoff) {}
