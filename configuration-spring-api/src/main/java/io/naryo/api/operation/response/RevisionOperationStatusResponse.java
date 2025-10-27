package io.naryo.api.operation.response;

import java.time.Instant;
import java.util.UUID;

import io.naryo.api.utils.IsoDateFormat;
import io.naryo.application.configuration.revision.RevisionOperationState;
import io.naryo.application.configuration.revision.RevisionOperationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Revision operation status")
public record RevisionOperationStatusResponse(
        UUID operationId,
        RevisionOperationState state,
        Long revision,
        String hash,
        String errorCode,
        String errorMessage,
        @IsoDateFormat Instant acceptedAt,
        @IsoDateFormat Instant lastUpdatedAt) {
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
