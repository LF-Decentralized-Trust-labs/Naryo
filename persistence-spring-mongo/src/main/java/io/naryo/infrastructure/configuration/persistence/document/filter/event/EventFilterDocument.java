package io.naryo.infrastructure.configuration.persistence.document.filter.event;

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
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public abstract class EventFilterDocument extends FilterDocument implements EventFilterDescriptor {

    @NotNull
    private EventFilterScope scope;

    @NotNull
    private EventFilterSpecificationDocument specification;

    @NotNull
    private List<ContractEventStatus> statuses;

    @Nullable
    private FilterSyncDocument sync;

    @NotNull
    private EventFilterVisibilityConfigurationDocument visibility;

    public EventFilterDocument(String id,
                               String name,
                               String nodeId,
                               EventFilterScope scope,
                               EventFilterSpecificationDocument specification,
                               List<ContractEventStatus> statuses,
                               @Nullable FilterSyncDocument sync,
                               EventFilterVisibilityConfigurationDocument visibility) {
        super(id, name, FilterType.EVENT, nodeId);
        this.scope = scope;
        this.specification = specification;
        this.statuses = statuses;
        this.sync = sync;
        this.visibility = visibility;
    }

    @Override
    public void setSpecification(EventSpecificationDescriptor specification) {
        this.specification = new EventFilterSpecificationDocument(
                specification.getSignature(),
                specification.getCorrelationId()
        );
    }

    @Override
    public void setSync(FilterSyncDescriptor sync) {
        if (sync instanceof BlockFilterSyncDescriptor blockFilterSyncDescriptor) {
            this.sync = new BlockFilterSyncDocument(blockFilterSyncDescriptor.getInitialBlock());
        } else {
            throw new IllegalArgumentException("Unsupported sync descriptor" + sync.getStrategy());
        }
    }

    @Override
    public void setVisibility(FilterVisibilityDescriptor visibility) {
        this.visibility = new EventFilterVisibilityConfigurationDocument(
                visibility.getVisible(),
                visibility.getPrivacyGroupId()
        );
    }

}
