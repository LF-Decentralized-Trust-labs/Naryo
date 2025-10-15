package io.naryo.api.filter.update.model;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.transaction.IdentifierType;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Valid
@Getter
public final class UpdateTransactionFilterRequest extends UpdateFilterRequest {

    @Schema(example = "TRANSACTION", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String type = "TRANSACTION";

    @NotNull
    private final IdentifierType identifierType;

    @NotBlank
    private final String value;

    private final Set<TransactionStatus> statuses;

    public UpdateTransactionFilterRequest(String name, UUID nodeId, String prevItemHash, IdentifierType identifierType, String value, Set<TransactionStatus> statuses) {
        super(name, nodeId, prevItemHash);
        this.identifierType = identifierType;
        this.value = value;
        this.statuses = statuses;
    }

    @Override
    public TransactionFilter toDomain(UUID idFromPath) {
        return new TransactionFilter(
                idFromPath,
                new FilterName(name),
                nodeId,
                identifierType,
                value,
                statuses);
    }
}
