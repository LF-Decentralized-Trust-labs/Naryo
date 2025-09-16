package io.naryo.infrastructure.configuration.source.env.model.node.connection;

import java.time.Duration;
import java.util.Optional;

import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import jakarta.annotation.Nullable;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public final class NodeConnectionRetryProperties implements NodeConnectionRetryDescriptor {

    private @Setter @Nullable Integer times;
    private @Setter @Nullable Duration backoff;

    public NodeConnectionRetryProperties(Integer times, Duration backoff) {
        this.times = times;
        this.backoff = backoff;
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
