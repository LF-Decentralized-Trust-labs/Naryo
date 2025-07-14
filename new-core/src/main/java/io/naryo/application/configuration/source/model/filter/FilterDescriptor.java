package io.naryo.application.configuration.source.model.filter;

import java.util.UUID;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.filter.FilterType;

public interface FilterDescriptor extends MergeableDescriptor<FilterDescriptor> {

    UUID getId();

    String getName();

    FilterType getType();

    UUID getNodeId();

    void setName(String name);

    void setNodeId(UUID nodeId);

    @Override
    default FilterDescriptor merge(FilterDescriptor descriptor) {
        if (descriptor == null) {
            return this;
        }

        if (!this.getName().equals(descriptor.getName())) {
            this.setName(descriptor.getName());
        }

        if (!this.getNodeId().equals(descriptor.getNodeId())) {
            this.setNodeId(descriptor.getNodeId());
        }

        return this;
    }
}
