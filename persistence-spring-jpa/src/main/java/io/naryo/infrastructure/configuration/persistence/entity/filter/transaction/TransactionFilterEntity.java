package io.naryo.infrastructure.configuration.persistence.entity.filter.transaction;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import io.naryo.application.configuration.source.model.filter.transaction.TransactionFilterDescriptor;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.transaction.IdentifierType;
import io.naryo.infrastructure.configuration.persistence.entity.filter.FilterEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("transaction")
@NoArgsConstructor
public class TransactionFilterEntity extends FilterEntity implements TransactionFilterDescriptor {

    private @Column(name = "identifier_type") @Nullable @Setter IdentifierType identifierType;
    private @Column(name = "value") @Nullable @Setter String value;
    private @ElementCollection(fetch = FetchType.EAGER) @CollectionTable(
            name = "transaction_filter_statuses") @Enumerated(EnumType.STRING) @Setter Set<
                    TransactionStatus>
            statuses;

    public TransactionFilterEntity(
            UUID id,
            String name,
            UUID nodeId,
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

    @Override
    public Set<TransactionStatus> getStatuses() {
        return statuses == null ? Set.of() : statuses;
    }
}
