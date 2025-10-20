package io.naryo.api.filter.update.model;

import java.util.Set;
import java.util.UUID;

import io.naryo.api.filter.common.request.EventFilterSpecificationRequest;
import io.naryo.api.filter.common.request.EventFilterVisibilityConfigurationRequest;
import io.naryo.api.filter.common.request.FilterSyncStateRequest;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.GlobalEventFilter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Valid
@Getter
public final class UpdateGlobalEventFilterRequest extends UpdateFilterRequest {

    private final @NotNull @Valid EventFilterSpecificationRequest specification;

    private final Set<ContractEventStatus> statuses;

    private final @NotNull @Valid FilterSyncStateRequest filterSyncState;

    private final @Valid EventFilterVisibilityConfigurationRequest visibilityConfiguration;

    public UpdateGlobalEventFilterRequest(
            String name,
            UUID nodeId,
            String prevItemHash,
            EventFilterSpecificationRequest specification,
            Set<ContractEventStatus> statuses,
            FilterSyncStateRequest filterSyncState,
            EventFilterVisibilityConfigurationRequest visibilityConfiguration) {
        super(name, nodeId, prevItemHash);
        this.specification = specification;
        this.statuses = statuses;
        this.filterSyncState = filterSyncState;
        this.visibilityConfiguration = visibilityConfiguration;
    }

    @Override
    public GlobalEventFilter toDomain(UUID idFromPath) {
        return new GlobalEventFilter(
                idFromPath,
                new FilterName(name),
                nodeId,
                specification.toDomain(),
                statuses,
                filterSyncState.toDomain(),
                visibilityConfiguration != null ? visibilityConfiguration.toDomain() : null);
    }
}
