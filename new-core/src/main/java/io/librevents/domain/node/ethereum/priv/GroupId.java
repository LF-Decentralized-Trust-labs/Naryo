package io.librevents.domain.node.ethereum.priv;

import java.util.Objects;

public record GroupId(String value) {
    public GroupId {
        Objects.requireNonNull(value, "Value must not be null");
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Value must not be empty");
        }
    }
}
