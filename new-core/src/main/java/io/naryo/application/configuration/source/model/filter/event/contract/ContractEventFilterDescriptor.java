package io.naryo.application.configuration.source.model.filter.event.contract;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.application.configuration.source.model.filter.event.EventFilterDescriptor;

import java.util.Optional;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface ContractEventFilterDescriptor extends EventFilterDescriptor {

    Optional<String> getAddress();

    void setAddress(Optional<String> address);

    @Override
    default FilterDescriptor merge(FilterDescriptor descriptor) {
        var filter = EventFilterDescriptor.super.merge(descriptor);

        if (filter instanceof ContractEventFilterDescriptor other) {
            mergeOptionals(this::setAddress, this.getAddress(), other.getAddress());
        }

        return filter;
    }
}
