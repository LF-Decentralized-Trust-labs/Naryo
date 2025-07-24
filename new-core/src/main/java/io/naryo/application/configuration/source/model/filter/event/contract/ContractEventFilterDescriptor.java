package io.naryo.application.configuration.source.model.filter.event.contract;

import java.util.Optional;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.application.configuration.source.model.filter.event.EventFilterDescriptor;
import io.naryo.domain.filter.event.EventFilterScope;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface ContractEventFilterDescriptor extends EventFilterDescriptor {

    Optional<String> getAddress();

    void setAddress(String address);

    @Override
    default EventFilterScope getScope() {
        return EventFilterScope.CONTRACT;
    }

    @Override
    default FilterDescriptor merge(FilterDescriptor other) {
        if (!(other instanceof ContractEventFilterDescriptor otherContractEventFilter)) {
            return this;
        }

        mergeOptionals(this::setAddress, this.getAddress(), otherContractEventFilter.getAddress());

        return EventFilterDescriptor.super.merge(other);
    }
}
