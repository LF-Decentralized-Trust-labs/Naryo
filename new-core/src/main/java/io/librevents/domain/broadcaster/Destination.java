package io.librevents.domain.broadcaster;

import java.util.Objects;

public record Destination(String value) {
    public Destination {
        Objects.requireNonNull(value, "value cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("value cannot be blank");
        }
    }
}
