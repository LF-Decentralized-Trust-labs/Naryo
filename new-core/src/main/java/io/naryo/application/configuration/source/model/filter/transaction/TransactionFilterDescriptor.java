package io.naryo.application.configuration.source.model.filter.transaction;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.transaction.IdentifierType;

import java.util.List;
import java.util.Optional;

import static io.naryo.application.common.util.MergeUtil.mergeLists;
import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface TransactionFilterDescriptor extends FilterDescriptor {

    Optional<IdentifierType> getIdentifierType();

    Optional<String> getValue();

    List<TransactionStatus> getStatuses();

    void setIdentifierType(Optional<IdentifierType> identifierType);

    void setValue(Optional<String> value);

    void setStatuses(List<TransactionStatus> statuses);

    @Override
    default FilterDescriptor merge(FilterDescriptor descriptor) {
        var filter = FilterDescriptor.super.merge(descriptor);

        if (filter instanceof TransactionFilterDescriptor other) {
            mergeOptionals(this::setIdentifierType, this.getIdentifierType(), other.getIdentifierType());
            mergeOptionals(this::setValue, this.getValue(), other.getValue());
            mergeLists(this::setStatuses, this.getStatuses(), other.getStatuses());
        }

        return filter;
    }
}
