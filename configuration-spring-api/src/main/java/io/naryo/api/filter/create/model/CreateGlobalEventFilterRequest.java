package io.naryo.api.filter.create.model;

import io.naryo.api.filter.create.model.CreateFilterRequest;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.EventFilterSpecification;
import io.naryo.domain.filter.event.EventFilterVisibilityConfiguration;
import io.naryo.domain.filter.event.FilterSyncState;
import io.naryo.domain.filter.event.GlobalEventFilter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Getter
public final class CreateGlobalEventFilterRequest extends CreateFilterRequest {

    @Schema(example = "GLOBAL-EVENT")
    private final String type = "GLOBAL-EVENT";

    @NotNull
    private final EventFilterSpecification specification;

    private final Set<ContractEventStatus> statuses;

    @NotNull
    private final FilterSyncState filterSyncState;

    private final EventFilterVisibilityConfiguration visibilityConfiguration;

    public CreateGlobalEventFilterRequest(String name,
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
    public Filter toDomain() {
        return new GlobalEventFilter(
            UUID.randomUUID(),
            new FilterName(this.name),
            this.nodeId,
            this.specification,
            this.statuses,
            this.filterSyncState,
            this.visibilityConfiguration
        );
    }
}
