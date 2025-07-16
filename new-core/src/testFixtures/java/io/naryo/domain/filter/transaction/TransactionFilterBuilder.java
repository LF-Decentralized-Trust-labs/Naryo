package io.naryo.domain.filter.transaction;

import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.FilterBuilder;
import io.naryo.domain.filter.FilterType;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import java.util.List;

import static org.instancio.Select.field;

public class TransactionFilterBuilder
    extends FilterBuilder<TransactionFilterBuilder, TransactionFilter> {

    private IdentifierType identifierType;
    private String value;
    private List<TransactionStatus> statuses;

    @Override
    public TransactionFilterBuilder self() {
        return this;
    }

    public TransactionFilter build() {
        InstancioApi<TransactionFilter> builder = Instancio.of(TransactionFilter.class);

        return super.buildBase(builder, FilterType.TRANSACTION)
            .set(field(TransactionFilter::getIdentifierType), this.getIdentifierType())
            .set(field(TransactionFilter::getValue), this.getValue())
            .set(field(TransactionFilter::getStatuses), this.getStatuses())
            .create();
    }

    public TransactionFilterBuilder withIdentifierType(IdentifierType identifierType) {
        this.identifierType = identifierType;
        return self();
    }

    public TransactionFilterBuilder withStatuses(List<TransactionStatus> statuses) {
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

    private List<TransactionStatus> getStatuses() {
        return this.statuses == null || this.statuses.isEmpty()
            ? List.of(Instancio.create(TransactionStatus.class))
            : this.statuses;
    }

    private String getValue() {
        return this.value == null
            ? Instancio.create(String.class)
            : this.value;
    }
}
