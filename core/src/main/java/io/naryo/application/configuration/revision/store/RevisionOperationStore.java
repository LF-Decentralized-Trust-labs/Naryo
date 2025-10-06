package io.naryo.application.configuration.revision.store;

import java.util.Optional;

import io.naryo.application.configuration.revision.RevisionOperationStatus;

public interface RevisionOperationStore {

    void accepted(String operationId);

    void running(String operationId);

    void succeeded(String operationId, long revision, String hash);

    void failed(String operationId, String errorCode, String errorMessage);

    Optional<RevisionOperationStatus> get(String operationId);
}
