package io.naryo.infrastructure.configuration.source.env.model.filter.transaction;

import io.naryo.application.configuration.source.model.filter.transaction.TransactionFilterDescriptor;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.FilterType;
import io.naryo.domain.filter.transaction.IdentifierType;
import io.naryo.infrastructure.configuration.source.env.model.filter.FilterProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Setter
@Getter
public final class TransactionFilterProperties extends FilterProperties
        implements TransactionFilterDescriptor {

    private Optional<IdentifierType> identifierType;
    private Optional<String> value;
    private List<TransactionStatus> statuses;

    public TransactionFilterProperties(
            UUID id,
            String name,
            UUID nodeId,
            IdentifierType identifierType,
            String value,
            List<TransactionStatus> statuses) {
        super(id, name, FilterType.TRANSACTION, nodeId);
        this.identifierType = Optional.of(identifierType);
        this.value = Optional.of(value);
        this.statuses = statuses;
    }
}
