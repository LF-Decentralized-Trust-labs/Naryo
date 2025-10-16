package io.naryo.api.filter.common.response;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.transaction.IdentifierType;
import lombok.Getter;

@Getter
public final class TransactionFilterResponse extends FilterResponse {

    private final IdentifierType identifierType;
    private final String value;
    private final Set<TransactionStatus> statuses;

    public TransactionFilterResponse(
            UUID id,
            String name,
            UUID nodeId,
            IdentifierType identifierType,
            String value,
            Set<TransactionStatus> statuses,
            String currentItemHash) {
        super(id, name, nodeId, currentItemHash);
        this.identifierType = identifierType;
        this.value = value;
        this.statuses = statuses;
    }
}
