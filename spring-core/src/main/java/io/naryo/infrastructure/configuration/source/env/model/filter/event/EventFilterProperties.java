package io.naryo.infrastructure.configuration.source.env.model.filter.event;

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
import io.naryo.domain.filter.FilterType;
import io.naryo.domain.filter.event.EventFilterScope;
import io.naryo.infrastructure.configuration.source.env.model.filter.FilterProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.FilterSyncProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.block.BlockFilterSyncProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.visibility.EventFilterVisibilityProperties;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;

@Getter
@Setter
public abstract class EventFilterProperties extends FilterProperties
        implements EventFilterDescriptor {

    private @Nullable EventFilterScope scope;
    private @Nullable EventSpecification specification;
    private Set<ContractEventStatus> statuses;
    private @Nullable FilterSyncProperties sync;
    private @Nullable EventFilterVisibilityProperties visibility;

    public EventFilterProperties(
            UUID id,
            String name,
            UUID nodeId,
            EventFilterScope scope,
            EventSpecification specification,
            Set<ContractEventStatus> statuses,
            FilterSyncProperties sync,
            EventFilterVisibilityProperties visibility) {
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
    public Optional<EventSpecification> getSpecification() {
        return Optional.ofNullable(specification);
    }

    @Override
    public void setSpecification(EventSpecificationDescriptor specification) {
        this.specification =
                new EventSpecification(
                        valueOrNull(specification.getSignature()),
                        valueOrNull(specification.getCorrelationId()));
    }

    @Override
    public Optional<FilterSyncProperties> getSync() {
        return Optional.ofNullable(sync);
    }

    @Override
    public void setSync(FilterSyncDescriptor sync) {
        if (sync instanceof BlockFilterSyncDescriptor blockFilterSyncDescriptor) {
            this.sync =
                    new BlockFilterSyncProperties(
                            valueOrNull(blockFilterSyncDescriptor.getInitialBlock()));
        } else {
            throw new IllegalArgumentException("Unknown sync strategy: " + sync.getStrategy());
        }
    }

    @Override
    public Optional<EventFilterVisibilityProperties> getVisibility() {
        return Optional.ofNullable(visibility);
    }

    @Override
    public void setVisibility(FilterVisibilityDescriptor visibility) {
        this.visibility =
                new EventFilterVisibilityProperties(
                        valueOrNull(visibility.getVisible()),
                        valueOrNull(visibility.getPrivacyGroupId()));
    }
}
