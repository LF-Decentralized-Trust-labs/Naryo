package io.naryo.api.filter.create;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.transaction.IdentifierType;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTransactionFilterRequest(
        @Schema(example = "transaction") String type,
        @NotBlank String name,
        @NotNull UUID nodeId,
        @NotNull IdentifierType identifierType,
        @NotBlank String value,
        Set<TransactionStatus> statuses)
        implements CreateFilterRequest {

    public CreateTransactionFilterRequest {
        type = "transaction";
    }

    public static TransactionFilter toDomain(CreateTransactionFilterRequest req) {
        var name = new FilterName(req.name());
        return new TransactionFilter(
                UUID.randomUUID(),
                name,
                req.nodeId(),
                req.identifierType(),
                req.value(),
                req.statuses());
    }
}
