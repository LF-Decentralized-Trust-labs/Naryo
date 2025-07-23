package io.naryo.application.configuration.source.model.filter.event;

import java.util.Optional;
import java.util.Set;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.application.configuration.source.model.filter.event.sync.FilterSyncDescriptor;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterType;
import io.naryo.domain.filter.event.EventFilterScope;

import static io.naryo.application.common.util.MergeUtil.*;

public interface EventFilterDescriptor extends FilterDescriptor {

    EventFilterScope getScope();

    Set<ContractEventStatus> getStatuses();

    <T extends EventSpecificationDescriptor> Optional<T> getSpecification();

    <T extends FilterSyncDescriptor> Optional<T> getSync();

    <T extends FilterVisibilityDescriptor> Optional<T> getVisibility();

    void setStatuses(Set<ContractEventStatus> statuses);

    void setSpecification(EventSpecificationDescriptor specification);

    void setSync(FilterSyncDescriptor sync);

    void setVisibility(FilterVisibilityDescriptor visibility);

    @Override
    default FilterType getType() {
        return FilterType.EVENT;
    }

    @Override
    default FilterDescriptor merge(FilterDescriptor descriptor) {
        FilterDescriptor.super.merge(descriptor);

        if (descriptor instanceof EventFilterDescriptor other) {
            mergeCollections(this::setStatuses, this.getStatuses(), other.getStatuses());
            mergeDescriptors(
                    this::setSpecification, this.getSpecification(), other.getSpecification());
            mergeDescriptors(this::setSync, this.getSync(), other.getSync());
            mergeDescriptors(this::setVisibility, this.getVisibility(), other.getVisibility());
        }

        return this;
    }
}
