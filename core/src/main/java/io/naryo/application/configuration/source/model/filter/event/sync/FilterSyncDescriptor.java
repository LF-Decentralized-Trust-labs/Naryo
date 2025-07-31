package io.naryo.application.configuration.source.model.filter.event.sync;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.domain.filter.event.sync.SyncStrategy;

public interface FilterSyncDescriptor extends MergeableDescriptor<FilterSyncDescriptor> {

    SyncStrategy getStrategy();

    @Override
    default FilterSyncDescriptor merge(FilterSyncDescriptor other) {
        if (!this.getStrategy().equals(other.getStrategy())) {
            return other;
        }

        return this;
    }
}
