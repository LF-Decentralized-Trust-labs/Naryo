package io.naryo.infrastructure.configuration.source.env.model.node.connection;

import java.time.Duration;
import java.util.Optional;

import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import jakarta.annotation.Nullable;
import lombok.Setter;

public final class NodeConnectionRetryProperties implements NodeConnectionRetryDescriptor {

    private static final Integer DEFAULT_RETRY_TIMES = 3;
    private static final Duration DEFAULT_BACKOFF = Duration.ofSeconds(30);

    private @Setter @Nullable Integer times;
    private @Setter @Nullable Duration backoff;

    public NodeConnectionRetryProperties(Integer times, Duration backoff) {
        this.times = times != null? times : DEFAULT_RETRY_TIMES;
        this.backoff = backoff != null? backoff : DEFAULT_BACKOFF;
    }

    public NodeConnectionRetryProperties() {
        this(DEFAULT_RETRY_TIMES, DEFAULT_BACKOFF);
    }

    @Override
    public Optional<Integer> getTimes() {
        return Optional.ofNullable(times);
    }

    @Override
    public Optional<Duration> getBackoff() {
        return Optional.ofNullable(backoff);
    }
}
