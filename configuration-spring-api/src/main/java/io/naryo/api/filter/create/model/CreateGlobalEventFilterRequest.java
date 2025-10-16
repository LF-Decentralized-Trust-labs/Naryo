package io.naryo.api.filter.create.model;

import java.util.Set;
import java.util.UUID;

import io.naryo.api.filter.common.request.EventFilterSpecificationRequest;
import io.naryo.api.filter.common.request.EventFilterVisibilityConfigurationRequest;
import io.naryo.api.filter.common.request.FilterSyncStateRequest;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.GlobalEventFilter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Valid
@Getter
public final class CreateGlobalEventFilterRequest extends CreateFilterRequest {

    private final @NotNull @Valid EventFilterSpecificationRequest specification;

    private final Set<ContractEventStatus> statuses;

    private final @NotNull @Valid FilterSyncStateRequest filterSyncState;

    private final @Valid EventFilterVisibilityConfigurationRequest visibilityConfiguration;

    public CreateGlobalEventFilterRequest(
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

    @Override
    public GlobalEventFilter toDomain() {
        return new GlobalEventFilter(
                UUID.randomUUID(),
                new FilterName(name),
                this.nodeId,
                this.specification.toDomain(),
                this.statuses,
                this.filterSyncState.toDomain(),
                this.visibilityConfiguration != null ? this.visibilityConfiguration.toDomain() : null);
    }
}
