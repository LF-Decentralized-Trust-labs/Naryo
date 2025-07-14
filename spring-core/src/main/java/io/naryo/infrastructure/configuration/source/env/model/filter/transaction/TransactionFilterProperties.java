package io.naryo.infrastructure.configuration.source.env.model.filter.transaction;

import java.util.List;
import java.util.UUID;

import io.naryo.application.configuration.source.model.filter.transaction.TransactionFilterDescriptor;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.FilterType;
import io.naryo.domain.filter.transaction.IdentifierType;
import io.naryo.infrastructure.configuration.source.env.model.filter.FilterProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public final class TransactionFilterProperties extends FilterProperties
        implements TransactionFilterDescriptor {

    private @Getter @Setter @NotNull IdentifierType identifierType;
    private @Getter @Setter @NotBlank String value;
    private @Getter @Setter @NotEmpty List<TransactionStatus> statuses;

    public TransactionFilterProperties(
            UUID id,
            String name,
            UUID nodeId,
            IdentifierType identifierType,
            String value,
            List<TransactionStatus> statuses) {
        super(id, name, FilterType.TRANSACTION, nodeId);
        this.identifierType = identifierType;
        this.value = value;
        this.statuses = statuses;

        if (this.statuses == null || this.statuses.isEmpty()) {
            this.statuses = List.of(TransactionStatus.values());
        }
    }
}
