package io.naryo.application.configuration.revision.operation;

import java.util.UUID;

public record RemoveOperation<T>(UUID id, String prevItemHash) implements RevisionOperation<T> {
    @Override
    public RevisionOperationKind kind() {
        return RevisionOperationKind.REMOVE;
    }
}
