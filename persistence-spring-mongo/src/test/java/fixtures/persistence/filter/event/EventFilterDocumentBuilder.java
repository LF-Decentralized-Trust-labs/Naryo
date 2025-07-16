package fixtures.persistence.filter.event;

import fixtures.persistence.filter.FilterDocumentBuilder;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.event.EventFilterScope;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.EventFilterDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.EventFilterSpecificationDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.EventFilterVisibilityConfigurationDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.syncstate.SyncStateDocument;
import org.instancio.Instancio;

import java.util.List;

public abstract class EventFilterDocumentBuilder<T, Y extends EventFilterDocument>
    extends FilterDocumentBuilder<T, Y> {

    private EventFilterScope scope;
    private EventFilterSpecificationDocument specification;
    private List<ContractEventStatus> statuses;
    private SyncStateDocument syncState;
    private EventFilterVisibilityConfigurationDocument visibilityConfiguration;

    public T withScope(EventFilterScope scope) {
        this.scope = scope;
        return this.self();
    }

    public T withSpecification(EventFilterSpecificationDocument specification) {
        this.specification = specification;
        return this.self();
    }

    public T withStatuses(List<ContractEventStatus> statuses) {
        this.statuses = statuses;
        return this.self();
    }

    public T withSyncState(SyncStateDocument syncState) {
        this.syncState = syncState;
        return this.self();
    }

    public T withVisibilityConfiguration(EventFilterVisibilityConfigurationDocument visibilityConfiguration) {
        this.visibilityConfiguration = visibilityConfiguration;
        return this.self();
    }

    protected EventFilterScope getScope() {
        return this.scope == null
            ? Instancio.create(EventFilterScope.class)
            : this.scope;
    }

    protected EventFilterSpecificationDocument getSpecification() {
        return this.specification == null
            ? new EventFilterSpecificationDocumentBuilder().build()
            : this.specification;
    }

    protected List<ContractEventStatus> getStatuses() {
        return this.statuses == null
            ? Instancio.createList(ContractEventStatus.class)
            : this.statuses;
    }

    protected SyncStateDocument getSyncState() {
        return this.syncState == null
            ? new BlockActiveSyncStateDocumentBuilder().build()
            : this.syncState;
    }

    protected EventFilterVisibilityConfigurationDocument getVisibilityConfiguration() {
        return this.visibilityConfiguration == null
            ? new EventFilterVisibilityConfigurationDocumentBuilder().build()
            : this.visibilityConfiguration;
    }
}
