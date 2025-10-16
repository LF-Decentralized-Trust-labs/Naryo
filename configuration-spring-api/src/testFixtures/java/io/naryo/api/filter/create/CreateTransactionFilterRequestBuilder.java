package io.naryo.api.filter.create;

import io.naryo.api.filter.create.model.CreateTransactionFilterRequest;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.transaction.IdentifierType;
import org.instancio.Instancio;

import java.util.Set;

public class CreateTransactionFilterRequestBuilder extends CreateFilterRequestBuilder<CreateTransactionFilterRequestBuilder, CreateTransactionFilterRequest> {

    private IdentifierType identifierType;
    private String value;
    private Set<TransactionStatus> statuses;

    public CreateTransactionFilterRequestBuilder withIdentifierType(IdentifierType identifierType) {
        this.identifierType = identifierType;
        return self();
    }

    public IdentifierType getIdentifierType() {
        return this.identifierType == null ? Instancio.create(IdentifierType.class) : this.identifierType;
    }

    public CreateTransactionFilterRequestBuilder withValue(String value) {
        this.value = value;
        return self();
    }

    public String getValue() {
        return this.value == null ? Instancio.create(String.class) : this.value;
    }

    public CreateTransactionFilterRequestBuilder withStatuses(Set<TransactionStatus> statuses) {
        this.statuses = statuses;
        return self();
    }

    public Set<TransactionStatus> getStatuses() {
        return this.statuses == null
            ? Instancio.ofSet(TransactionStatus.class).size(2).create()
            : this.statuses;
    }

    @Override
    public CreateTransactionFilterRequestBuilder self() {
        return this;
    }

    @Override
    public CreateTransactionFilterRequest build() {
        return new CreateTransactionFilterRequest(
            getName(),
            getNodeId(),
            getIdentifierType(),
            getValue(),
            getStatuses()
        );
    }
}
