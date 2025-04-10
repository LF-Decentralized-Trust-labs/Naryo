package io.librevents.domain.node.connection.http;

import java.time.Duration;

public record ConnectionTimeout(Duration value) {
    public ConnectionTimeout {
        if (value == null || value.isNegative() || value.isZero()) {
            throw new IllegalArgumentException("Must be > 0");
        }
    }
}
