package io.librevents.domain.node.subscription.block.method.poll;

import java.time.Duration;

public record Interval(Duration value) {
    public Interval {
        if (value == null || value.isNegative()) {
            throw new IllegalArgumentException("Must be positive");
        }
    }
}
