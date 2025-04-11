package io.librevents.domain.filter.transaction;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import io.librevents.domain.filter.Filter;
import io.librevents.domain.filter.FilterName;
import io.librevents.domain.filter.FilterType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class TransactionFilter extends Filter {

    private final IdentifierType identifierType;
    private final String value;
    private final List<TransactionStatus> statuses;

    public TransactionFilter(
            UUID id,
            FilterName name,
            UUID nodeId,
            IdentifierType identifierType,
            String value,
            List<TransactionStatus> statuses) {
        super(id, name, FilterType.TRANSACTION, nodeId);
        this.identifierType = identifierType;
        this.value = value;
        this.statuses = statuses;

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
}
