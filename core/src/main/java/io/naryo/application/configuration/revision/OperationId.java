package io.naryo.application.configuration.revision;

import java.util.Objects;

public final class OperationId {

    private final String value;

    public OperationId(String id) {
        this.value = Objects.requireNonNull(id);
    }

    public static OperationId random() {
        return new OperationId(java.util.UUID.randomUUID().toString());
    }

    public static OperationId of(String id) {
        return new OperationId(id);
    }

    public String getValue() {
        return this.value;
    }
}
