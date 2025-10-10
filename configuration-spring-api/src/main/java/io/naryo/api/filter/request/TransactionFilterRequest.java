package io.naryo.api.filter.request;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.transaction.IdentifierType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransactionFilterRequest(
        @Schema(example = "transaction") String type,
        @NotNull UUID id,
        @NotBlank String name,
        @NotNull UUID nodeId,
        @NotNull IdentifierType identifierType,
        @NotBlank String value,
        Set<TransactionStatus> statuses)
        implements FilterRequest {

    public TransactionFilterRequest {
        type = "transaction";
        if (statuses == null || statuses.isEmpty()) {
            statuses = Set.of(TransactionStatus.values());
        }
    }
}
