package io.naryo.api.filter.create.model;

import java.util.Set;
import java.util.UUID;

import io.naryo.api.filter.common.request.EventFilterSpecificationRequest;
import io.naryo.api.filter.common.request.FilterSyncStateRequest;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Valid
@Getter
public final class CreateContractEventFilterRequest extends CreateFilterRequest {

    private final @NotNull @Valid EventFilterSpecificationRequest specification;

    private final Set<ContractEventStatus> statuses;

    private final @NotNull @Valid FilterSyncStateRequest filterSyncState;

    private final String contractAddress;

    public CreateContractEventFilterRequest(
            String name,
            UUID nodeId,
            EventFilterSpecificationRequest specification,
            Set<ContractEventStatus> statuses,
            FilterSyncStateRequest filterSyncState,
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
                this.specification.toDomain(),
                this.statuses,
                this.filterSyncState.toDomain(),
                this.contractAddress);
    }
}
