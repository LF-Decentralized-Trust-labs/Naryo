package io.naryo.application.configuration.revision;

import java.util.Objects;
import java.util.UUID;

public record OperationId(UUID value) {

    public OperationId {
        Objects.requireNonNull(value, "OperationId must not be null");
    }

    public static OperationId random() {
        return new OperationId(UUID.randomUUID());
    }
}
