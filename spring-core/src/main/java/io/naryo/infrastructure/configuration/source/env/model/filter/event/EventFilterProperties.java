package io.naryo.infrastructure.configuration.source.env.model.filter.event;

import java.util.List;
import java.util.UUID;

import io.naryo.application.configuration.source.model.filter.event.EventFilterDescriptor;
import io.naryo.application.configuration.source.model.filter.event.EventSpecificationDescriptor;
import io.naryo.application.configuration.source.model.filter.event.FilterVisibilityDescriptor;
import io.naryo.application.configuration.source.model.filter.event.sync.FilterSyncDescriptor;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterType;
import io.naryo.domain.filter.event.EventFilterScope;
import io.naryo.infrastructure.configuration.source.env.model.filter.FilterProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.FilterSyncProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.visibility.EventFilterVisibilityProperties;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public abstract class EventFilterProperties extends FilterProperties
        implements EventFilterDescriptor {

    private final @Getter @NotNull EventFilterScope scope;
    private @Getter @Valid @NotNull EventSpecification specification;
    private @Getter @Setter @NotEmpty List<ContractEventStatus> statuses;
    private @Getter @Valid @Nullable FilterSyncProperties sync;
    private @Getter @Valid @NotNull EventFilterVisibilityProperties visibility;

    public EventFilterProperties(
            UUID id,
            String name,
            UUID nodeId,
            EventFilterScope scope,
            EventSpecification specification,
            List<ContractEventStatus> statuses,
            FilterSyncProperties sync,
            EventFilterVisibilityProperties visibility) {
        super(id, name, FilterType.EVENT, nodeId);
        this.scope = scope;
        this.specification = specification;
        this.statuses =
                statuses == null || statuses.isEmpty()
                        ? List.of(ContractEventStatus.values())
                        : statuses;
        this.sync = sync;
        this.visibility = visibility == null ? new EventFilterVisibilityProperties() : visibility;
    }

    @Override
    public void setSpecification(EventSpecificationDescriptor specification) {
        this.specification = (EventSpecification) specification;
    }

    @Override
    public void setSync(FilterSyncDescriptor sync) {
        this.sync = (FilterSyncProperties) sync;
    }

    @Override
    public void setVisibility(FilterVisibilityDescriptor visibility) {
        this.visibility = (EventFilterVisibilityProperties) visibility;
    }
}
