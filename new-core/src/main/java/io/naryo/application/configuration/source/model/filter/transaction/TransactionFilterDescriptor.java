package io.naryo.application.configuration.source.model.filter.transaction;

import java.util.Optional;
import java.util.Set;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.FilterType;
import io.naryo.domain.filter.transaction.IdentifierType;

import static io.naryo.application.common.util.MergeUtil.mergeCollections;
import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface TransactionFilterDescriptor extends FilterDescriptor {

    Optional<IdentifierType> getIdentifierType();

    Optional<String> getValue();

    Set<TransactionStatus> getStatuses();

    void setIdentifierType(IdentifierType identifierType);

    void setValue(String value);

    void setStatuses(Set<TransactionStatus> statuses);

    @Override
    default FilterType getType() {
        return FilterType.TRANSACTION;
    }

    @Override
    default FilterDescriptor merge(FilterDescriptor other) {
        if (!(other instanceof TransactionFilterDescriptor otherTransactionFilter)) {
            return this;
        }

        mergeOptionals(
                this::setIdentifierType,
                this.getIdentifierType(),
                otherTransactionFilter.getIdentifierType());
        mergeOptionals(this::setValue, this.getValue(), otherTransactionFilter.getValue());
        mergeCollections(
                this::setStatuses, this.getStatuses(), otherTransactionFilter.getStatuses());

        return FilterDescriptor.super.merge(otherTransactionFilter);
    }
}
