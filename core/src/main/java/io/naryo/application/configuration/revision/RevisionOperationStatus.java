package io.naryo.application.configuration.revision;

import java.time.Instant;
import java.util.UUID;

public record RevisionOperationStatus(
        UUID operationId,
        RevisionOperationState state,
        Long revision,
        String hash,
        String errorCode,
        String errorMessage,
        Instant acceptedAt,
        Instant lastUpdatedAt) {}
