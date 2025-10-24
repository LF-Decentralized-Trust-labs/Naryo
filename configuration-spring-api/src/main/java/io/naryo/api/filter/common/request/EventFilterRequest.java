package io.naryo.api.filter.common.request;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(description = "Base class for event filter request")
@Valid
@Getter
public abstract class EventFilterRequest extends FilterRequest {

    protected final @NotNull @Valid EventFilterSpecificationRequest specification;

    protected final Set<ContractEventStatus> statuses;

    protected final @NotNull @Valid FilterSyncStateRequest filterSyncState;

    protected final @Valid EventFilterVisibilityConfigurationRequest visibilityConfiguration;

    public EventFilterRequest(
            String name,
            UUID nodeId,
            EventFilterSpecificationRequest specification,
            Set<ContractEventStatus> statuses,
            FilterSyncStateRequest filterSyncState,
            EventFilterVisibilityConfigurationRequest visibilityConfiguration) {
        super(name, nodeId);
        this.specification = specification;
        this.statuses = statuses;
        this.filterSyncState = filterSyncState;
        this.visibilityConfiguration = visibilityConfiguration;
    }
}
