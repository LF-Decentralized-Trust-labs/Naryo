package io.naryo.domain.filter.transaction;

import io.naryo.application.node.interactor.block.dto.Transaction;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.FilterType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class TransactionFilter extends Filter {

    private final IdentifierType identifierType;
    private final String value;
    private final Set<TransactionStatus> statuses;

    public TransactionFilter(
            UUID id,
            FilterName name,
            UUID nodeId,
            IdentifierType identifierType,
            String value,
            Set<TransactionStatus> statuses) {
        super(id, name, FilterType.TRANSACTION, nodeId);
        this.identifierType = identifierType;
        this.value = value;
        this.statuses = statuses.isEmpty() ? Set.of(TransactionStatus.values()) : statuses;

        validInvariants();
    }

    private void validInvariants() {
        Objects.requireNonNull(identifierType, "identifierType must not be null");
        Objects.requireNonNull(value, "value must not be null");
        Objects.requireNonNull(statuses, "statuses must not be null");

        if (statuses.isEmpty()) {
            throw new IllegalArgumentException("statuses must not be empty");
        }

        if (value.isEmpty()) {
            throw new IllegalArgumentException("value must not be empty");
        }
    }

    public boolean matches(Transaction transaction) {
        return switch (identifierType) {
            case HASH -> transaction.hash().equals(value);
            case TO_ADDRESS -> transaction.to().equals(value);
            case FROM_ADDRESS -> transaction.from().equals(value);
        };
    }
}
