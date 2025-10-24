package io.naryo.api.filter.common.request;

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

@Schema(description = "Transaction filter request")
@Valid
@Getter
public final class TransactionFilterRequest extends FilterRequest {

    private final @NotNull IdentifierType identifierType;

    private final @NotBlank String value;

    private final Set<TransactionStatus> statuses;

    public TransactionFilterRequest(
            String name,
            UUID nodeId,
            IdentifierType identifierType,
            String value,
            Set<TransactionStatus> statuses) {
        super(name, nodeId);
        this.identifierType = identifierType;
        this.value = value;
        this.statuses = statuses;
    }

    @Override
    public TransactionFilter toDomain(UUID id) {
        return new TransactionFilter(
                id,
                new FilterName(this.name),
                this.nodeId,
                this.identifierType,
                this.value,
                this.statuses);
    }
}
