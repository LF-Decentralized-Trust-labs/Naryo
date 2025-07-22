package io.naryo.infrastructure.configuration.persistence.document.filter.event;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import io.naryo.application.configuration.source.model.filter.event.EventFilterDescriptor;
import io.naryo.application.configuration.source.model.filter.event.EventSpecificationDescriptor;
import io.naryo.application.configuration.source.model.filter.event.FilterVisibilityDescriptor;
import io.naryo.application.configuration.source.model.filter.event.sync.BlockFilterSyncDescriptor;
import io.naryo.application.configuration.source.model.filter.event.sync.FilterSyncDescriptor;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterType;
import io.naryo.domain.filter.event.EventFilterScope;
import io.naryo.infrastructure.configuration.persistence.document.filter.FilterDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.sync.BlockFilterSyncDocument;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.sync.FilterSyncDocument;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;

public abstract class EventFilterDocument extends FilterDocument implements EventFilterDescriptor {

    @Nullable @Setter private EventFilterScope scope;

    @Nullable private EventSpecificationDocument specification;

    @Getter @Setter private Set<ContractEventStatus> statuses;

    @Nullable private FilterSyncDocument sync;

    @Nullable private FilterVisibilityDocument visibility;

    public EventFilterDocument(
            String id,
            String name,
            String nodeId,
            EventFilterScope scope,
            EventSpecificationDocument specification,
            Set<ContractEventStatus> statuses,
            FilterSyncDocument sync,
            FilterVisibilityDocument visibility) {
        super(id, name, FilterType.EVENT, nodeId);
        this.scope = scope;
        this.specification = specification;
        this.statuses = statuses == null ? new HashSet<>() : statuses;
        this.sync = sync;
        this.visibility = visibility;
    }

    @Override
    public Optional<EventFilterScope> getScope() {
        return Optional.ofNullable(scope);
    }

    @Override
    public Optional<EventSpecificationDocument> getSpecification() {
        return Optional.ofNullable(specification);
    }

    @Override
    public void setSpecification(EventSpecificationDescriptor specification) {
        this.specification =
                new EventSpecificationDocument(
                        valueOrNull(specification.getSignature()),
                        valueOrNull(specification.getCorrelationId()));
    }

    @Override
    public Optional<FilterSyncDocument> getSync() {
        return Optional.ofNullable(sync);
    }

    @Override
    public void setSync(FilterSyncDescriptor sync) {
        if (sync instanceof BlockFilterSyncDescriptor blockFilterSyncDescriptor) {
            this.sync =
                    new BlockFilterSyncDocument(
                            valueOrNull(blockFilterSyncDescriptor.getInitialBlock()));
        } else {
            throw new IllegalArgumentException("Unknown sync strategy: " + sync.getStrategy());
        }
    }

    @Override
    public Optional<FilterVisibilityDocument> getVisibility() {
        return Optional.ofNullable(visibility);
    }

    @Override
    public void setVisibility(FilterVisibilityDescriptor visibility) {
        this.visibility =
                new FilterVisibilityDocument(
                        valueOrNull(visibility.getVisible()),
                        valueOrNull(visibility.getPrivacyGroupId()));
    }
}
