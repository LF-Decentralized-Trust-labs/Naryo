package io.naryo.api.filter.create.model;

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
public final class CreateGlobalEventFilterRequest extends CreateFilterRequest {

    @Schema(example = "GLOBAL-EVENT", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String type = "GLOBAL-EVENT";

    private final @NotNull EventFilterSpecification specification;

    private final Set<ContractEventStatus> statuses;

    private final @NotNull FilterSyncState filterSyncState;

    private final EventFilterVisibilityConfiguration visibilityConfiguration;

    public CreateGlobalEventFilterRequest(
            String name,
            UUID nodeId,
            EventFilterSpecification specification,
            Set<ContractEventStatus> statuses,
            FilterSyncState filterSyncState,
            EventFilterVisibilityConfiguration visibilityConfiguration) {
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
                this.specification,
                this.statuses,
                this.filterSyncState,
                this.visibilityConfiguration);
    }
}
