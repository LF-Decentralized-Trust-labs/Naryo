package io.naryo.application.configuration.revision;

import java.util.UUID;

import io.naryo.application.configuration.revision.operation.RevisionOperationKind;
import lombok.Getter;

@Getter
public final class RevisionConflictException extends RuntimeException {

    private final UUID id;
    private final RevisionOperationKind operationKind;

    public RevisionConflictException(UUID id, RevisionOperationKind operationKind, String message) {
        super(message);
        this.id = id;
        this.operationKind = operationKind;
    }
}
