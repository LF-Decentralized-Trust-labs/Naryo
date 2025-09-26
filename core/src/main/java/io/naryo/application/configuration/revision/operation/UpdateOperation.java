package io.naryo.application.configuration.revision.operation;

public record UpdateOperation<T>(String id, String prevItemHash, T proposed)
        implements RevisionOperation<T> {
    @Override
    public RevisionOperationKind kind() {
        return RevisionOperationKind.UPDATE;
    }
}
