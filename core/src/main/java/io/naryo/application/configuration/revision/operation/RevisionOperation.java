package io.naryo.application.configuration.revision.operation;

public sealed interface RevisionOperation<T>
        permits AddOperation, UpdateOperation, RemoveOperation {
    RevisionOperationKind kind();
}
