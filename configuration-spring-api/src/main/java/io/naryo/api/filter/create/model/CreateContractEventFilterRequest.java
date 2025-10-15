package io.naryo.api.filter.create.model;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Valid
@Getter
public final class CreateContractEventFilterRequest extends CreateFilterRequest {

    @Schema(example = "CONTRACT-EVENT", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String type = "CONTRACT-EVENT";

    private final @NotNull EventFilterSpecification specification;

    private final Set<ContractEventStatus> statuses;

    private final @NotNull FilterSyncState filterSyncState;

    private final String contractAddress;

    public CreateContractEventFilterRequest(
            String name,
            UUID nodeId,
            EventFilterSpecification specification,
            Set<ContractEventStatus> statuses,
            FilterSyncState filterSyncState,
            String contractAddress) {
        super(name, nodeId);
        this.specification = specification;
        this.statuses = statuses;
        this.filterSyncState = filterSyncState;
        this.contractAddress = contractAddress;
    }

    @Override
    public ContractEventFilter toDomain() {
        return new ContractEventFilter(
                UUID.randomUUID(),
                new FilterName(this.name),
                this.nodeId,
                this.specification,
                this.statuses,
                this.filterSyncState,
                this.contractAddress);
    }
}
