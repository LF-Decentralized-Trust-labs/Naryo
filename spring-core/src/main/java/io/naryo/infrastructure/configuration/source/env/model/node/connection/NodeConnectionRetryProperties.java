package io.naryo.infrastructure.configuration.source.env.model.node.connection;

import java.time.Duration;

import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public final class NodeConnectionRetryProperties implements NodeConnectionRetryDescriptor {

    private static final Integer DEFAULT_RETRY_TIMES = 3;
    private static final Duration DEFAULT_BACKOFF = Duration.ofSeconds(30);

    private @Getter @Setter Integer times;
    private @Getter @Setter @NotNull Duration backoff;

    public NodeConnectionRetryProperties(int times, Duration backoff) {
        this.times = times;
        this.backoff = backoff;
    }

    public NodeConnectionRetryProperties() {
        this(DEFAULT_RETRY_TIMES, DEFAULT_BACKOFF);
    }
}
