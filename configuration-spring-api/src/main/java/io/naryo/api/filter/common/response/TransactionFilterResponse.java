package io.naryo.api.filter.common.response;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.transaction.IdentifierType;
import lombok.Builder;

@Builder
public record TransactionFilterResponse(
        UUID id,
        String name,
        UUID nodeId,
        IdentifierType identifierType,
        String value,
        Set<TransactionStatus> statuses,
        String currentItemHash)
        implements FilterResponse {}
