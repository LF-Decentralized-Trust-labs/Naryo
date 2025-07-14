package io.naryo.application.configuration.source.model.filter.event;

import java.util.List;

import io.naryo.application.configuration.source.model.filter.FilterDescriptor;
import io.naryo.application.configuration.source.model.filter.event.sync.FilterSyncDescriptor;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.event.EventFilterScope;

public interface EventFilterDescriptor extends FilterDescriptor {

    EventFilterScope getScope();

    EventSpecificationDescriptor getSpecification();

    List<ContractEventStatus> getStatuses();

    FilterSyncDescriptor getSync();

    FilterVisibilityDescriptor getVisibility();

    void setSpecification(EventSpecificationDescriptor specification);

    void setStatuses(List<ContractEventStatus> statuses);

    void setSync(FilterSyncDescriptor sync);

    void setVisibility(FilterVisibilityDescriptor visibility);

    @Override
    default FilterDescriptor merge(FilterDescriptor descriptor) {
        var filter = FilterDescriptor.super.merge(descriptor);

        if (filter instanceof EventFilterDescriptor other) {
            if (!this.getScope().equals(other.getScope())) {
                return filter;
            }

            if (!this.getSpecification().equals(other.getSpecification())) {
                this.setSpecification(other.getSpecification());
            }

            if (!this.getStatuses().equals(other.getStatuses())) {
                this.setStatuses(other.getStatuses());
            }

            if (!this.getSync().equals(other.getSync())) {
                this.setSync(other.getSync());
            }

            if (!this.getVisibility().equals(other.getVisibility())) {
                this.setVisibility(other.getVisibility());
            }
        }

        return filter;
    }
}
