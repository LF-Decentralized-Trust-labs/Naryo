package io.naryo.application.configuration.revision.operation;

public record RemoveOperation<T>(String id, String prevItemHash) implements RevisionOperation<T> {
    @Override
    public RevisionOperationKind kind() {
        return RevisionOperationKind.REMOVE;
    }
}
