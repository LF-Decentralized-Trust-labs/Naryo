package io.librevents.domain.node.connection;

import java.time.Duration;

public record RetryConfiguration(int times, Duration backoff) {

    public RetryConfiguration {
        if (times < 0) {
            throw new IllegalArgumentException("Retry times must be >= 0");
        }
        if (backoff == null || backoff.isNegative() || backoff.isZero()) {
            throw new IllegalArgumentException("Backoff duration must be > 0");
        }
    }

}
