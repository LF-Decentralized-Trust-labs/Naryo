package io.naryo.domain.filter.event;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterBuilder;
import org.instancio.Instancio;

import java.util.List;

public abstract class EventFilterBuilder<T, Y extends EventFilter>
    extends FilterBuilder<T, Y> {

    private EventFilterSpecification specification;
    private List<ContractEventStatus> statuses;
    private SyncState syncState;
    private EventFilterVisibilityConfiguration visibilityConfiguration;

    public T withSpecification(EventFilterSpecification specification) {
        this.specification = specification;
        return this.self();
    }

    public T withStatuses(List<ContractEventStatus> statuses) {
        this.statuses = statuses;
        return this.self();
    }

    public T withSyncState(SyncState syncState) {
        this.syncState = syncState;
        return this.self();
    }

    public T withVisibilityConfiguration(EventFilterVisibilityConfiguration visibilityConfiguration) {
        this.visibilityConfiguration = visibilityConfiguration;
        return this.self();
    }

    protected EventFilterSpecification getSpecification() {
        return this.specification == null
            ? new EventFilterSpecificationBuilder().build()
            : this.specification;
    }

    protected List<ContractEventStatus> getStatuses() {
        return this.statuses == null
            ? Instancio.createList(ContractEventStatus.class)
            : this.statuses;
    }

    protected SyncState getSyncState() {
        return this.syncState == null
            ? new BlockActiveSyncStateBuilder().build()
            : this.syncState;
    }

    protected EventFilterVisibilityConfiguration getVisibilityConfiguration() {
        return this.visibilityConfiguration == null
            ? new EventFilterVisibilityConfigurationBuilder().build()
            : this.visibilityConfiguration;
    }
}
