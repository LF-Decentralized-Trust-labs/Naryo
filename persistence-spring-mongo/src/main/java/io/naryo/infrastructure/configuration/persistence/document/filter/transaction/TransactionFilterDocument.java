package io.naryo.infrastructure.configuration.persistence.document.filter.transaction;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import io.naryo.application.configuration.source.model.filter.transaction.TransactionFilterDescriptor;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.transaction.IdentifierType;
import io.naryo.infrastructure.configuration.persistence.document.filter.FilterDocument;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("transaction_filter")
@Setter
public class TransactionFilterDocument extends FilterDocument
        implements TransactionFilterDescriptor {

    private @Nullable IdentifierType identifierType;

    private @Nullable String value;

    private @Getter Set<TransactionStatus> statuses;

    public TransactionFilterDocument(
            String id,
            String name,
            String nodeId,
            IdentifierType identifierType,
            String value,
            Set<TransactionStatus> statuses) {
        super(id, name, nodeId);
        this.identifierType = identifierType;
        this.value = value;
        this.statuses = statuses == null ? new HashSet<>() : statuses;
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
