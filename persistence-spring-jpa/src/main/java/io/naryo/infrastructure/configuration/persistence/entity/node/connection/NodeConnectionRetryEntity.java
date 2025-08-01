package io.naryo.infrastructure.configuration.persistence.entity.node.connection;

import java.time.Duration;
import java.util.Optional;

import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Setter;

@Embeddable
public class NodeConnectionRetryEntity implements NodeConnectionRetryDescriptor {

    private static final int DEFAULT_RETRY_TIMES = 3;
    private static final Duration DEFAULT_BACKOFF = Duration.ofSeconds(30);

    private @Setter @Nullable @Column(name = "times") Integer times;
    private @Setter @Nullable @Column(name = "backoff") Duration backoff;

    public NodeConnectionRetryEntity(@Nullable Integer times, @Nullable Duration backoff) {
        this.times = times != null ? times : DEFAULT_RETRY_TIMES;
        this.backoff = backoff != null ? backoff : DEFAULT_BACKOFF;
    }

    public NodeConnectionRetryEntity() {
        this(DEFAULT_RETRY_TIMES, DEFAULT_BACKOFF);
    }

    @Override
    public Optional<Integer> getTimes() {
        return Optional.ofNullable(this.times);
    }

    @Override
    public Optional<Duration> getBackoff() {
        return Optional.ofNullable(this.backoff);
    }
}
