package io.naryo.application.configuration.revision.store;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.revision.RevisionOperationStatus;

public interface RevisionOperationStore {

    void accepted(UUID operationId);

    void running(UUID operationId);

    void succeeded(UUID operationId, long revision, String hash);

    void failed(UUID operationId, String errorCode, String errorMessage);

    Optional<RevisionOperationStatus> get(UUID operationId);
}
