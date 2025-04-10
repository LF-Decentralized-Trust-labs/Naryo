package io.librevents.domain.node;

import java.util.Objects;

public record NodeName(String value) {

    public NodeName {
        Objects.requireNonNull(value);
        if (value.isEmpty()) throw new IllegalArgumentException("Value cannot be empty");
    }
}
