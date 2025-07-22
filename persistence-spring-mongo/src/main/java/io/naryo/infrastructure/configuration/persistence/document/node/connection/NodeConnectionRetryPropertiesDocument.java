package io.naryo.infrastructure.configuration.persistence.document.node.connection;

import java.time.Duration;
import java.util.Optional;

import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import jakarta.annotation.Nullable;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("connection_retry")
public final class NodeConnectionRetryPropertiesDocument implements NodeConnectionRetryDescriptor {

    private static final int DEFAULT_RETRY_TIMES = 3;
    private static final Duration DEFAULT_BACKOFF = Duration.ofSeconds(30);

    private @Setter @Nullable Integer times;
    private @Setter @Nullable Duration backoff;

    public NodeConnectionRetryPropertiesDocument(Integer times, Duration backoff) {
        this.times = times;
        this.backoff = backoff;
    }

    public NodeConnectionRetryPropertiesDocument() {
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
