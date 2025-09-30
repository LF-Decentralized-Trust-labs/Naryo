package io.naryo.application.configuration.revision.operation;

public record AddOperation<T>(T item) implements RevisionOperation<T> {
    @Override
    public RevisionOperationKind kind() {
        return RevisionOperationKind.ADD;
    }
}
