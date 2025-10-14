package io.naryo.api.filter.request;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.transaction.IdentifierType;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateTransactionFilterRequest(
        @Schema(example = "transaction") String type,
        @NotBlank String name,
        @NotNull UUID nodeId,
        @NotNull IdentifierType identifierType,
        @NotBlank String value,
        Set<TransactionStatus> statuses,
        @NotBlank String prevItemHash)
        implements UpdateFilterRequest {

    public UpdateTransactionFilterRequest {
        type = "transaction";
    }

    public static TransactionFilter toDomain(UpdateTransactionFilterRequest req, UUID idFromPath) {
        return new TransactionFilter(
                idFromPath,
                new FilterName(req.name()),
                req.nodeId(),
                req.identifierType(),
                req.value(),
                req.statuses());
    }
}
