package io.naryo.infrastructure.configuration.persistence.document.node.connection;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

import java.time.Duration;

@TypeAlias("connection")
public class RetryPropertiesDocument {

    private static final int DEFAULT_RETRY_TIMES = 3;
    private static final Duration DEFAULT_BACKOFF = Duration.ofSeconds(30);

    private final int times;
    @NotNull private final Duration backoff;

    public RetryPropertiesDocument(int times, Duration backoff) {
        this.times = times;
        this.backoff = backoff;
    }

    public RetryPropertiesDocument() {
        this(DEFAULT_RETRY_TIMES, DEFAULT_BACKOFF);
    }

    public int getTimes() {
        return times != 0 ? times : DEFAULT_RETRY_TIMES;
    }

    public Duration getBackoff() {
        return backoff != null ? backoff : DEFAULT_BACKOFF;
    }
}
