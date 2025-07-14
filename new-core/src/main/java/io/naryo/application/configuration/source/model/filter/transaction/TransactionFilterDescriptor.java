package io.naryo.application.configuration.source.model.filter.transaction;

import java.util.List;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.domain.common.TransactionStatus;
import io.naryo.domain.filter.transaction.IdentifierType;

public interface TransactionFilterDescriptor extends FilterDescriptor {

    IdentifierType getIdentifierType();

    String getValue();

    List<TransactionStatus> getStatuses();

    void setIdentifierType(IdentifierType identifierType);

    void setValue(String value);

    void setStatuses(List<TransactionStatus> statuses);

    @Override
    default FilterDescriptor merge(FilterDescriptor descriptor) {
        var filter = FilterDescriptor.super.merge(descriptor);

        if (filter instanceof TransactionFilterDescriptor other) {
            if (!this.getIdentifierType().equals(other.getIdentifierType())) {
                this.setIdentifierType(other.getIdentifierType());
            }

            if (!this.getValue().equals(other.getValue())) {
                this.setValue(other.getValue());
            }

            if (!this.getStatuses().equals(other.getStatuses())) {
                this.setStatuses(other.getStatuses());
            }
        }

        return filter;
    }
}
