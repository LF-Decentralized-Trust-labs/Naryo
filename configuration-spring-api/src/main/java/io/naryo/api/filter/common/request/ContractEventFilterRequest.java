package io.naryo.api.filter.common.request;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Getter;

@Schema(description = "Contract event filter request")
@Valid
@Getter
public final class ContractEventFilterRequest extends EventFilterRequest {

    private final String contractAddress;

    public ContractEventFilterRequest(
            String name,
            UUID nodeId,
            EventFilterSpecificationRequest specification,
            Set<ContractEventStatus> statuses,
            FilterSyncStateRequest filterSyncState,
            EventFilterVisibilityConfigurationRequest visibilityConfiguration,
            String contractAddress) {
        super(name, nodeId, specification, statuses, filterSyncState, visibilityConfiguration);
        this.contractAddress = contractAddress;
    }

    @Override
    public ContractEventFilter toDomain(UUID id) {
        return new ContractEventFilter(
                id,
                new FilterName(this.name),
                this.nodeId,
                this.specification.toDomain(),
                this.statuses,
                this.filterSyncState.toDomain(),
                this.contractAddress);
    }
}
