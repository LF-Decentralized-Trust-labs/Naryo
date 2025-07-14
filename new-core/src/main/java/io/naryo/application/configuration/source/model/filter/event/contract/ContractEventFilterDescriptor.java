package io.naryo.application.configuration.source.model.filter.event.contract;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.application.configuration.source.model.filter.event.EventFilterDescriptor;

public interface ContractEventFilterDescriptor extends EventFilterDescriptor {

    String getAddress();

    void setAddress(String address);

    @Override
    default FilterDescriptor merge(FilterDescriptor descriptor) {
        var filter = EventFilterDescriptor.super.merge(descriptor);

        if (filter instanceof ContractEventFilterDescriptor other) {
            if (!this.getAddress().equals(other.getAddress())) {
                this.setAddress(other.getAddress());
            }
        }

        return filter;
    }
}
