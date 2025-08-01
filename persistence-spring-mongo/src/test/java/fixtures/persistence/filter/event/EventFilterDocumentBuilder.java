package fixtures.persistence.filter.event;

import java.util.Set;

import fixtures.persistence.filter.FilterDocumentBuilder;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.EventFilterDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.EventSpecificationDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.FilterVisibilityDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.sync.FilterSyncDocument;
import org.instancio.Instancio;

public abstract class EventFilterDocumentBuilder<T, Y extends EventFilterDocument>
        extends FilterDocumentBuilder<T, Y> {

    private EventSpecificationDocument specification;
    private Set<ContractEventStatus> statuses;
    private FilterSyncDocument sync;
    private FilterVisibilityDocument visibility;

    public T withSpecification(EventSpecificationDocument specification) {
        this.specification = specification;
        return this.self();
    }

    public T withStatuses(Set<ContractEventStatus> statuses) {
        this.statuses = statuses;
        return this.self();
    }

    public T withSync(FilterSyncDocument sync) {
        this.sync = sync;
        return this.self();
    }

    public T withVisibility(FilterVisibilityDocument visibilityConfiguration) {
        this.visibility = visibilityConfiguration;
        return this.self();
    }

    protected EventSpecificationDocument getSpecification() {
        return this.specification == null
                ? new EventFilterSpecificationDocumentBuilder().build()
                : this.specification;
    }

    protected Set<ContractEventStatus> getStatuses() {
        return this.statuses == null
                ? Instancio.createSet(ContractEventStatus.class)
                : this.statuses;
    }

    protected FilterSyncDocument getSync() {
        return this.sync == null ? new BlockFilterSyncDocumentBuilder().build() : this.sync;
    }

    protected FilterVisibilityDocument getVisibility() {
        return this.visibility == null
                ? new EventFilterVisibilityConfigurationDocumentBuilder().build()
                : this.visibility;
    }
}
