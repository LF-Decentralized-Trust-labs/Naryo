package io.naryo.api.filter.create.model;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.Filter;
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
public final class CreateTransactionFilterRequest extends CreateFilterRequest {

    @Schema(example = "TRANSACTION", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String type = "TRANSACTION";

    @NotNull
    private IdentifierType identifierType;

    @NotBlank
    private String value;

    private Set<TransactionStatus> statuses;

    public CreateTransactionFilterRequest(String name,
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
    public TransactionFilter toDomain() {
        return new TransactionFilter(
            UUID.randomUUID(),
            new FilterName(this.name),
            this.nodeId,
            this.identifierType,
            this.value,
            this.statuses
        );
    }
}
