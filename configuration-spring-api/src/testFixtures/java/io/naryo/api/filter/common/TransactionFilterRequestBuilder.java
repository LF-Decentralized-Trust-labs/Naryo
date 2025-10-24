package io.naryo.api.filter.common;

import java.util.Set;

import io.naryo.api.filter.common.request.TransactionFilterRequest;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.transaction.IdentifierType;
import org.instancio.Instancio;

public class TransactionFilterRequestBuilder
        extends FilterRequestBuilder<TransactionFilterRequestBuilder, TransactionFilterRequest> {

    private IdentifierType identifierType;
    private String value;
    private Set<TransactionStatus> statuses;

    public TransactionFilterRequestBuilder withIdentifierType(IdentifierType identifierType) {
        this.identifierType = identifierType;
        return self();
    }

    public IdentifierType getIdentifierType() {
        return this.identifierType == null
                ? Instancio.create(IdentifierType.class)
                : this.identifierType;
    }

    public TransactionFilterRequestBuilder withValue(String value) {
        this.value = value;
        return self();
    }

    public String getValue() {
        return this.value == null ? Instancio.create(String.class) : this.value;
    }

    public TransactionFilterRequestBuilder withStatuses(Set<TransactionStatus> statuses) {
        this.statuses = statuses;
        return self();
    }

    public Set<TransactionStatus> getStatuses() {
        return this.statuses == null
                ? Instancio.ofSet(TransactionStatus.class).size(2).create()
                : this.statuses;
    }

    @Override
    public TransactionFilterRequestBuilder self() {
        return this;
    }

    @Override
    public TransactionFilterRequest build() {
        return new TransactionFilterRequest(
                getName(), getNodeId(), getIdentifierType(), getValue(), getStatuses());
    }
}
