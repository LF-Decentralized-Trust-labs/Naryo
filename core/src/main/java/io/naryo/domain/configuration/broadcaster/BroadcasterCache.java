package io.naryo.domain.configuration.broadcaster;

import java.time.Duration;
import java.util.Objects;

public record BroadcasterCache(Duration expirationTime) {
    public BroadcasterCache {
        Objects.requireNonNull(expirationTime, "expirationTime must not be null");
        if (expirationTime.isNegative() || expirationTime.isZero()) {
            throw new IllegalArgumentException("expirationTime must be positive");
        }
    }
}
