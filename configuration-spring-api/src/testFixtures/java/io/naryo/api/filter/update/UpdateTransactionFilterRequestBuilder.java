package io.naryo.api.filter.update;

import io.naryo.api.filter.update.model.UpdateTransactionFilterRequest;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.transaction.IdentifierType;
import org.instancio.Instancio;

import java.util.Set;

public class UpdateTransactionFilterRequestBuilder extends UpdateFilterRequestBuilder<UpdateTransactionFilterRequestBuilder, UpdateTransactionFilterRequest> {

    private IdentifierType identifierType;
    private String value;
    private Set<TransactionStatus> statuses;

    public UpdateTransactionFilterRequestBuilder withIdentifierType(IdentifierType identifierType) {
        this.identifierType = identifierType;
        return self();
    }

    public UpdateTransactionFilterRequestBuilder withValue(String value) {
        this.value = value;
        return self();
    }

    public UpdateTransactionFilterRequestBuilder withStatuses(Set<TransactionStatus> statuses) {
        this.statuses = statuses;
        return self();
    }

    public IdentifierType getIdentifierType() {
        return this.identifierType == null ? Instancio.create(IdentifierType.class) : this.identifierType;
    }

    public String getValue() {
        return this.value == null ? Instancio.create(String.class) : this.value;
    }

    public Set<TransactionStatus> getStatuses() {
        return this.statuses == null ? Instancio.ofSet(TransactionStatus.class).size(2).create() : this.statuses;
    }

    @Override
    public UpdateTransactionFilterRequestBuilder self() {
        return this;
    }

    @Override
    public UpdateTransactionFilterRequest build() {
        return new UpdateTransactionFilterRequest(
            getName(),
            getNodeId(),
            getPrevItemHash(),
            getIdentifierType(),
            getValue(),
            getStatuses()
        );
    }
}
