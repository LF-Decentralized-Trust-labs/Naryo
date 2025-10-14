package io.naryo.api.operation.response;

import java.time.Instant;
import java.util.UUID;

import io.naryo.api.utils.IsoDateFormat;
import io.naryo.application.configuration.revision.RevisionOperationState;
import io.naryo.application.configuration.revision.RevisionOperationStatus;
import lombok.Getter;

@Getter
public class RevisionOperationStatusResponse {
    private final UUID operationId;
    private final RevisionOperationState state;
    private final Long revision;
    private final String hash;
    private final String errorCode;
    private final String errorMessage;
    private final @IsoDateFormat Instant acceptedAt;
    private final @IsoDateFormat Instant lastUpdatedAt;

    private RevisionOperationStatusResponse(
            UUID operationId,
            RevisionOperationState state,
            Long revision,
            String hash,
            String errorCode,
            String errorMessage,
            Instant acceptedAt,
            Instant lastUpdatedAt) {
        this.operationId = operationId;
        this.state = state;
        this.revision = revision;
        this.hash = hash;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.acceptedAt = acceptedAt;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public static RevisionOperationStatusResponse fromRevisionOperationStatus(
            RevisionOperationStatus revisionOperationStatus) {
        return new RevisionOperationStatusResponse(
                revisionOperationStatus.operationId(),
                revisionOperationStatus.state(),
                revisionOperationStatus.revision(),
                revisionOperationStatus.hash(),
                revisionOperationStatus.errorCode(),
                revisionOperationStatus.errorMessage(),
                revisionOperationStatus.acceptedAt(),
                revisionOperationStatus.lastUpdatedAt());
    }
}
