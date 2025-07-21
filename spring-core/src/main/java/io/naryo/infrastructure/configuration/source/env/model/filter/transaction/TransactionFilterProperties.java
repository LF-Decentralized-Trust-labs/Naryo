package io.naryo.infrastructure.configuration.source.env.model.filter.transaction;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import io.naryo.application.configuration.source.model.filter.transaction.TransactionFilterDescriptor;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.FilterType;
import io.naryo.domain.filter.transaction.IdentifierType;
import io.naryo.infrastructure.configuration.source.env.model.filter.FilterProperties;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Setter
public final class TransactionFilterProperties extends FilterProperties
        implements TransactionFilterDescriptor {

    private @Nullable IdentifierType identifierType;
    private @Nullable String value;
    private @Getter Set<TransactionStatus> statuses;

    public TransactionFilterProperties(
            UUID id,
            String name,
            UUID nodeId,
            IdentifierType identifierType,
            String value,
            Set<TransactionStatus> statuses) {
        super(id, name, FilterType.TRANSACTION, nodeId);
        this.identifierType = identifierType;
        this.value = value;
        this.statuses = statuses;
    }

    @Override
    public Optional<IdentifierType> getIdentifierType() {
        return Optional.ofNullable(identifierType);
    }

    @Override
    public Optional<String> getValue() {
        return Optional.ofNullable(value);
    }
}
