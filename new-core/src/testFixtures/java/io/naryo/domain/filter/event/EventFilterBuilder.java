package io.naryo.domain.filter.event;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterBuilder;
import io.naryo.domain.filter.FilterType;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import java.util.List;

import static org.instancio.Select.field;

public abstract class EventFilterBuilder<T, Y extends EventFilter>
    extends FilterBuilder<T, Y> {

    private EventFilterScope scope;
    private EventFilterSpecification specification;
    private List<ContractEventStatus> statuses;
    private SyncState syncState;
    private EventFilterVisibilityConfiguration visibilityConfiguration;

    public T withScope(EventFilterScope scope) {
        this.scope = scope;
        return this.self();
    }

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

    protected InstancioApi<Y> buildBase(InstancioApi<Y> builder) {
        return super.buildBase(builder, FilterType.EVENT)
            .set(field(EventFilter::getScope), this.getScope())
            .set(field(EventFilter::getSpecification), this.getSpecification())
            .set(field(EventFilter::getStatuses), this.getStatuses())
            .set(field(EventFilter::getSyncState), this.getSyncState())
            .set(field(EventFilter::getVisibilityConfiguration), this.getVisibilityConfiguration());
    }

    private EventFilterScope getScope() {
        return this.scope == null
            ? Instancio.create(EventFilterScope.class)
            : this.scope;
    }

    private EventFilterSpecification getSpecification() {
        return this.specification == null
            ? Instancio.create(EventFilterSpecification.class)
            : this.specification;
    }

    private List<ContractEventStatus> getStatuses() {
        return this.statuses == null
            ? Instancio.createList(ContractEventStatus.class)
            : this.statuses;
    }

    private SyncState getSyncState() {
        return this.syncState == null
            ? new BlockActiveSyncStateBuilder().build()
            : this.syncState;
    }

    private EventFilterVisibilityConfiguration getVisibilityConfiguration() {
        return this.visibilityConfiguration == null
            ? new EventFilterVisibilityConfigurationBuilder().build()
            : this.visibilityConfiguration;
    }
}
