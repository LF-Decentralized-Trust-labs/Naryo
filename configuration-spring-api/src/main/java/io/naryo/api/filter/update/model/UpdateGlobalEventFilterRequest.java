package io.naryo.api.filter.update.model;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.EventFilterSpecification;
import io.naryo.domain.filter.event.EventFilterVisibilityConfiguration;
import io.naryo.domain.filter.event.FilterSyncState;
import io.naryo.domain.filter.event.GlobalEventFilter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Valid
@Getter
public final class UpdateGlobalEventFilterRequest extends UpdateFilterRequest {

    @Schema(example = "GLOBAL-EVENT", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String type = "GLOBAL-EVENT";

    private final @NotNull EventFilterSpecification specification;

    private final Set<ContractEventStatus> statuses;

    private final @NotNull FilterSyncState filterSyncState;

    EventFilterVisibilityConfiguration visibilityConfiguration;

    public UpdateGlobalEventFilterRequest(
            String name,
            UUID nodeId,
            String prevItemHash,
            EventFilterSpecification specification,
            Set<ContractEventStatus> statuses,
            FilterSyncState filterSyncState,
            EventFilterVisibilityConfiguration visibilityConfiguration) {
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
                specification,
                statuses,
                filterSyncState,
                visibilityConfiguration);
    }
}
