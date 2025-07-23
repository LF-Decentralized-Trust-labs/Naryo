package io.naryo.application.configuration.source.model.filter;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.filter.FilterType;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface FilterDescriptor extends MergeableDescriptor<FilterDescriptor> {

    UUID getId();

    Optional<String> getName();

    FilterType getType();

    Optional<UUID> getNodeId();

    void setName(String name);

    void setNodeId(UUID nodeId);

    @Override
    default FilterDescriptor merge(FilterDescriptor descriptor) {
        if (descriptor == null) {
            return this;
        }

        mergeOptionals(this::setName, this.getName(), descriptor.getName());
        mergeOptionals(this::setNodeId, this.getNodeId(), descriptor.getNodeId());

        return this;
    }
}
