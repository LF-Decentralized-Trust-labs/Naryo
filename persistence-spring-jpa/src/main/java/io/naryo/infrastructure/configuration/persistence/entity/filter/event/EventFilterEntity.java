package io.naryo.infrastructure.configuration.persistence.entity.filter.event;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import io.naryo.application.configuration.source.model.filter.event.EventFilterDescriptor;
import io.naryo.application.configuration.source.model.filter.event.EventSpecificationDescriptor;
import io.naryo.application.configuration.source.model.filter.event.FilterVisibilityDescriptor;
import io.naryo.application.configuration.source.model.filter.event.sync.BlockFilterSyncDescriptor;
import io.naryo.application.configuration.source.model.filter.event.sync.FilterSyncDescriptor;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.infrastructure.configuration.persistence.entity.filter.FilterEntity;
import io.naryo.infrastructure.configuration.persistence.entity.filter.event.sync.BlockFilterSyncEntity;
import io.naryo.infrastructure.configuration.persistence.entity.filter.event.sync.FilterSyncEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;

@Entity
@NoArgsConstructor
public abstract class EventFilterEntity extends FilterEntity implements EventFilterDescriptor {

    private @Embedded @Nullable EventSpecification specification;

    private @ElementCollection(fetch = FetchType.EAGER) @CollectionTable(
            name = "event_filter_status",
            joinColumns = @JoinColumn(name = "event_filter_id")) @Enumerated(EnumType.STRING)
    @Column(name = "status") @Setter Set<ContractEventStatus> statuses;

    private @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = "sync_id") @Nullable
    FilterSyncEntity sync;

    private @Embedded @Nullable FilterVisibility visibility;

    public EventFilterEntity(
            UUID id,
            String name,
            UUID nodeId,
            EventSpecification specification,
            Set<ContractEventStatus> statuses,
            FilterSyncEntity sync,
            FilterVisibility visibility) {
        super(id, name, nodeId);
        this.specification = specification;
        this.statuses = statuses == null ? new HashSet<>() : statuses;
        this.sync = sync;
        this.visibility = visibility;
    }

    @Override
    public void setSpecification(EventSpecificationDescriptor specification) {
        this.specification =
                new EventSpecification(
                        valueOrNull(specification.getSignature()),
                        valueOrNull(specification.getCorrelationId()));
    }

    @Override
    public void setSync(FilterSyncDescriptor sync) {
        if (sync instanceof BlockFilterSyncDescriptor blockFilterSyncDescriptor) {
            this.sync =
                    new BlockFilterSyncEntity(
                            valueOrNull(blockFilterSyncDescriptor.getInitialBlock()));
        } else {
            throw new IllegalArgumentException("Unknown sync strategy: " + sync.getStrategy());
        }
    }

    @Override
    public void setVisibility(FilterVisibilityDescriptor visibility) {
        this.visibility =
                new FilterVisibility(
                        valueOrNull(visibility.getVisible()),
                        valueOrNull(visibility.getPrivacyGroupId()));
    }

    @Override
    public Optional<EventSpecification> getSpecification() {
        return Optional.ofNullable(specification);
    }

    @Override
    public Optional<FilterSyncEntity> getSync() {
        return Optional.ofNullable(sync);
    }

    @Override
    public Optional<FilterVisibility> getVisibility() {
        return Optional.ofNullable(visibility);
    }

    @Override
    public Set<ContractEventStatus> getStatuses() {
        return statuses == null ? Set.of() : statuses;
    }
}
