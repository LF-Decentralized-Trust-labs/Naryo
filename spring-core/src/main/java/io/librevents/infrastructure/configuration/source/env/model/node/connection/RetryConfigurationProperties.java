package io.librevents.infrastructure.configuration.source.env.model.node.connection;

import java.time.Duration;

import jakarta.validation.constraints.NotNull;

public record RetryConfigurationProperties(int times, @NotNull Duration backoff) {

    private static final int DEFAULT_RETRY_TIMES = 3;
    private static final Duration DEFAULT_BACKOFF = Duration.ofSeconds(30);

    public RetryConfigurationProperties() {
        this(DEFAULT_RETRY_TIMES, DEFAULT_BACKOFF);
    }
}
