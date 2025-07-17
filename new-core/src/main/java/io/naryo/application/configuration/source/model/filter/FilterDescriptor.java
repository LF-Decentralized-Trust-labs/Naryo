package io.naryo.application.configuration.source.model.filter;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.filter.FilterType;

import java.util.Optional;
import java.util.UUID;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface FilterDescriptor extends MergeableDescriptor<FilterDescriptor> {

    UUID getId();

    Optional<String> getName();

    Optional<FilterType> getType();

    Optional<UUID> getNodeId();

    void setName(Optional<String> name);

    void setType(Optional<FilterType> filterType);

    void setNodeId(Optional<UUID> nodeId);

    @Override
    default FilterDescriptor merge(FilterDescriptor descriptor) {
        if (descriptor == null) {
            return this;
        }

        mergeOptionals(this::setName, this.getName(), descriptor.getName());
        mergeOptionals(this::setType, this.getType(), descriptor.getType());
        mergeOptionals(this::setNodeId, this.getNodeId(), descriptor.getNodeId());

        return this;
    }


}
