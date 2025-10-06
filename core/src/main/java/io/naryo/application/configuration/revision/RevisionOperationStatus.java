package io.naryo.application.configuration.revision;

import java.time.Instant;

public record RevisionOperationStatus(
        String operationId,
        RevisionOperationState state,
        Long revision,
        String hash,
        String errorCode,
        String errorMessage,
        Instant acceptedAt,
        Instant lastUpdatedAt) {}
