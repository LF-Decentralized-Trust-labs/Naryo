package io.naryo.infrastructure.configuration.persistence.document.node.connection;

import java.time.Duration;

import io.naryo.application.configuration.source.model.node.connection.retry.NodeConnectionRetryDescriptor;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

@Setter
@TypeAlias("connection")
public class RetryPropertiesDocument implements NodeConnectionRetryDescriptor {

    private static final int DEFAULT_RETRY_TIMES = 3;
    private static final Duration DEFAULT_BACKOFF = Duration.ofSeconds(30);

    private Integer times;
    private @NotNull Duration backoff;

    public RetryPropertiesDocument(int times, Duration backoff) {
        this.times = times;
        this.backoff = backoff;
    }

    public RetryPropertiesDocument() {
        this(DEFAULT_RETRY_TIMES, DEFAULT_BACKOFF);
    }

    @Override
    public Integer getTimes() {
        return times != 0 ? times : DEFAULT_RETRY_TIMES;
    }

    @Override
    public Duration getBackoff() {
        return backoff != null ? backoff : DEFAULT_BACKOFF;
    }
}
