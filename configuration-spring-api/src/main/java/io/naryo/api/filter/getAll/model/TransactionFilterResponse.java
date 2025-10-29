package io.naryo.api.filter.getAll.model;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.FilterType;
import io.naryo.domain.filter.transaction.IdentifierType;
import io.naryo.domain.filter.transaction.TransactionFilter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Transaction filter")
@Getter
public final class TransactionFilterResponse extends FilterResponse {

    private final IdentifierType identifierType;
    private final String value;
    private final FilterType filterType = FilterType.TRANSACTION;
    private final Set<TransactionStatus> statuses;

    public TransactionFilterResponse(
            UUID id,
            FilterType type,
            String name,
            UUID nodeId,
            String currentItemHash,
            IdentifierType identifierType,
            String value,
            Set<TransactionStatus> statuses) {
        super(id, name, nodeId, currentItemHash);
        this.identifierType = identifierType;
        this.value = value;
        this.statuses = statuses;
    }

    public static TransactionFilterResponse fromDomain(
            TransactionFilter transactionFilter, String currentItemHash) {
        return new TransactionFilterResponse(
                transactionFilter.getId(),
                transactionFilter.getType(),
                transactionFilter.getName().value(),
                transactionFilter.getNodeId(),
                currentItemHash,
                transactionFilter.getIdentifierType(),
                transactionFilter.getValue(),
                transactionFilter.getStatuses());
    }
}
