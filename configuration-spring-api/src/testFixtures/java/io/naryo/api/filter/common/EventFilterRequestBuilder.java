package io.naryo.api.filter.common;

import java.util.Set;

import io.naryo.api.filter.common.request.*;
import io.naryo.domain.common.event.ContractEventStatus;
import org.instancio.Instancio;

public abstract class EventFilterRequestBuilder<
                T extends EventFilterRequestBuilder<T, Y>, Y extends EventFilterRequest>
        extends FilterRequestBuilder<T, Y> {

    private EventFilterSpecificationRequest specification;
    private Set<ContractEventStatus> statuses;
    private FilterSyncStateRequest filterSyncState;
    private EventFilterVisibilityConfigurationRequest visibilityConfiguration;

    public T withSpecification(EventFilterSpecificationRequest specification) {
        this.specification = specification;
        return self();
    }

    public T withStatuses(Set<ContractEventStatus> statuses) {
        this.statuses = statuses;
        return self();
    }

    public T withFilterSyncState(FilterSyncStateRequest filterSyncState) {
        this.filterSyncState = filterSyncState;
        return self();
    }

    public T withVisibilityConfiguration(
            EventFilterVisibilityConfigurationRequest visibilityConfiguration) {
        this.visibilityConfiguration = visibilityConfiguration;
        return self();
    }

    public EventFilterSpecificationRequest getSpecification() {
        return this.specification == null
                ? new EventFilterSpecificationRequestBuilder().build()
                : this.specification;
    }

    public Set<ContractEventStatus> getStatuses() {
        return this.statuses == null
                ? Instancio.ofSet(ContractEventStatus.class).size(2).create()
                : this.statuses;
    }

    public FilterSyncStateRequest getFilterSyncState() {
        return this.filterSyncState == null
                ? new FilterSyncStateRequestBuilder().build()
                : this.filterSyncState;
    }

    public EventFilterVisibilityConfigurationRequest getVisibilityConfiguration() {
        return this.visibilityConfiguration == null
                ? new EventFilterVisibilityConfigurationRequestBuilder().build()
                : this.visibilityConfiguration;
    }
}
