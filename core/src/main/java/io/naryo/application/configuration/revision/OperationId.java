package io.naryo.application.configuration.revision;

import java.util.Objects;
import java.util.UUID;

public final class OperationId {

    private final String value;

    public OperationId(String id) {
        String nonNullId = Objects.requireNonNull(id);
        UUID parsed;
        try {
            parsed = UUID.fromString(nonNullId);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("OperationId must be a valid UUID string", ex);
        }
        this.value = parsed.toString();
    }

    public static OperationId random() {
        return new OperationId(UUID.randomUUID().toString());
    }

    public static OperationId of(String id) {
        return new OperationId(id);
    }

    public String getValue() {
        return this.value;
    }
}
