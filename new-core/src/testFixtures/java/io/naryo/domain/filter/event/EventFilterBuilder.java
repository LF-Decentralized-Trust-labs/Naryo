package io.naryo.domain.filter.event;

import java.util.Set;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterBuilder;
import org.instancio.Instancio;

public abstract class EventFilterBuilder<T, Y extends EventFilter> extends FilterBuilder<T, Y> {

    private EventFilterSpecification specification;
    private Set<ContractEventStatus> statuses;
    private SyncState syncState;
    private EventFilterVisibilityConfiguration visibilityConfiguration;

    public T withSpecification(EventFilterSpecification specification) {
        this.specification = specification;
        return this.self();
    }

    public T withStatuses(Set<ContractEventStatus> statuses) {
        this.statuses = statuses;
        return this.self();
    }

    public T withSyncState(SyncState syncState) {
        this.syncState = syncState;
        return this.self();
    }

    public T withVisibilityConfiguration(
            EventFilterVisibilityConfiguration visibilityConfiguration) {
        this.visibilityConfiguration = visibilityConfiguration;
        return this.self();
    }

    protected EventFilterSpecification getSpecification() {
        return this.specification == null
                ? new EventFilterSpecificationBuilder().build()
                : this.specification;
    }

    protected Set<ContractEventStatus> getStatuses() {
        return this.statuses == null
                ? Instancio.createSet(ContractEventStatus.class)
                : this.statuses;
    }

    protected SyncState getSyncState() {
        return this.syncState == null ? new BlockActiveSyncStateBuilder().build() : this.syncState;
    }

    protected EventFilterVisibilityConfiguration getVisibilityConfiguration() {
        return this.visibilityConfiguration == null
                ? new EventFilterVisibilityConfigurationBuilder().build()
                : this.visibilityConfiguration;
    }
}
