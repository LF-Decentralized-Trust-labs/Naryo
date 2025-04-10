package io.librevents.domain.node.connection.http;

import java.time.Duration;

public record KeepAliveDuration(Duration value) {
    public KeepAliveDuration {
        if (value == null || value.isNegative()) {
            throw new IllegalArgumentException("Must be positive");
        }
    }
}
