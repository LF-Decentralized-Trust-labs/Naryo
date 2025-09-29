package io.naryo.application.configuration.revision.operation;

import java.util.UUID;

public record UpdateOperation<T>(UUID id, String prevItemHash, T proposed)
        implements RevisionOperation<T> {
    @Override
    public RevisionOperationKind kind() {
        return RevisionOperationKind.UPDATE;
    }
}
