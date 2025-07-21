package io.naryo.application.configuration.source.model.filter.event;

import java.util.Optional;
import java.util.Set;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.application.configuration.source.model.filter.event.sync.FilterSyncDescriptor;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.event.EventFilterScope;

import static io.naryo.application.common.util.MergeUtil.*;

public interface EventFilterDescriptor extends FilterDescriptor {

    Optional<EventFilterScope> getScope();

    Set<ContractEventStatus> getStatuses();

    <T extends EventSpecificationDescriptor> Optional<T> getSpecification();

    <T extends FilterSyncDescriptor> Optional<T> getSync();

    <T extends FilterVisibilityDescriptor> Optional<T> getVisibility();

    void setScope(EventFilterScope scope);

    void setStatuses(Set<ContractEventStatus> statuses);

    void setSpecification(EventSpecificationDescriptor specification);

    void setSync(FilterSyncDescriptor sync);

    void setVisibility(FilterVisibilityDescriptor visibility);

    @Override
    default FilterDescriptor merge(FilterDescriptor descriptor) {
        var filter = FilterDescriptor.super.merge(descriptor);

        if (filter instanceof EventFilterDescriptor other) {
            mergeOptionals(this::setScope, this.getScope(), other.getScope());
            mergeCollections(this::setStatuses, this.getStatuses(), other.getStatuses());
            mergeDescriptors(
                    this::setSpecification, this.getSpecification(), other.getSpecification());
            mergeDescriptors(this::setSync, this.getSync(), other.getSync());
            mergeDescriptors(this::setVisibility, this.getVisibility(), other.getVisibility());
        }

        return filter;
    }
}
