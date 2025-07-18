package io.naryo.infrastructure.configuration.source.env.model.filter.event;

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
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public abstract class EventFilterProperties extends FilterProperties
    implements EventFilterDescriptor {

    protected Optional<EventFilterScope> scope;
    private Optional<EventSpecification> specification;
    private Set<ContractEventStatus> statuses;
    protected Optional<FilterSyncProperties> sync;
    private Optional<EventFilterVisibilityProperties> visibility;

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
        this.scope = Optional.ofNullable(scope);
        this.specification = Optional.ofNullable(specification);
        this.statuses = statuses;
        this.sync = Optional.ofNullable(sync);
        this.visibility = Optional.ofNullable(visibility);
    }

    @Override
    public void setSpecification(Optional<EventSpecificationDescriptor> specification) {
        this.specification = specification.map(specificationDescriptor -> new EventSpecification(
            specificationDescriptor.getSignature(),
            specificationDescriptor.getCorrelationId()
        ));

    }

    @Override
    public void setSync(Optional<FilterSyncDescriptor> sync) {
        this.sync = sync.map(syncDescriptor -> {
            if (syncDescriptor instanceof BlockFilterSyncDescriptor blockFilterSyncDescriptor) {
                return new BlockFilterSyncProperties(blockFilterSyncDescriptor.getInitialBlock());
            } else {
                throw new IllegalArgumentException("Unknown sync strategy: " + syncDescriptor.getStrategy());
            }
        });
    }

    @Override
    public void setVisibility(Optional<FilterVisibilityDescriptor> visibility) {
        this.visibility = visibility.map(visibilityDescriptor ->
            new EventFilterVisibilityProperties(
                visibilityDescriptor.getVisible(),
                visibilityDescriptor.getPrivacyGroupId()
            )
        );
    }

}
