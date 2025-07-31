package io.naryo.domain.filter.transaction;

import java.util.Set;

import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.FilterBuilder;
import org.instancio.Instancio;

public class TransactionFilterBuilder
        extends FilterBuilder<TransactionFilterBuilder, TransactionFilter> {

    private IdentifierType identifierType;
    private String value;
    private Set<TransactionStatus> statuses;

    @Override
    public TransactionFilterBuilder self() {
        return this;
    }

    public TransactionFilter build() {
        return new TransactionFilter(
                this.getId(),
                this.getName(),
                this.getNodeId(),
                this.getIdentifierType(),
                this.getValue(),
                this.getStatuses());
    }

    public TransactionFilterBuilder withIdentifierType(IdentifierType identifierType) {
        this.identifierType = identifierType;
        return self();
    }

    public TransactionFilterBuilder withStatuses(Set<TransactionStatus> statuses) {
        this.statuses = statuses;
        return self();
    }

    public TransactionFilterBuilder withValue(String value) {
        this.value = value;
        return self();
    }

    private IdentifierType getIdentifierType() {
        return this.identifierType == null
                ? Instancio.create(IdentifierType.class)
                : this.identifierType;
    }

    private Set<TransactionStatus> getStatuses() {
        return this.statuses == null || this.statuses.isEmpty()
                ? Instancio.createSet(TransactionStatus.class)
                : this.statuses;
    }

    private String getValue() {
        return this.value == null ? Instancio.create(String.class) : this.value;
    }
}
