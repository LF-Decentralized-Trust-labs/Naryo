package io.naryo.api.filter.getAll.model;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.event.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Contract event filter")
@Getter
public final class ContractEventFilterResponse extends EventFilterResponse {

    private final String contractAddress;

    private ContractEventFilterResponse(
            UUID id,
            String name,
            UUID nodeId,
            String currentItemHash,
            EventFilterSpecificationResponse specification,
            Set<ContractEventStatus> statuses,
            FilterSyncStateResponse filterSyncState,
            EventFilterVisibilityConfigurationResponse visibilityConfiguration,
            String contractAddress) {
        super(
                id,
                name,
                nodeId,
                currentItemHash,
                specification,
                statuses,
                filterSyncState,
                visibilityConfiguration);
        this.contractAddress = contractAddress;
    }

    public static EventFilterResponse fromDomain(
            ContractEventFilter contractEventFilter, String currentItemHash) {
        return new ContractEventFilterResponse(
                contractEventFilter.getId(),
                contractEventFilter.getName().value(),
                contractEventFilter.getNodeId(),
                currentItemHash,
                EventFilterSpecificationResponse.fromDomain(contractEventFilter.getSpecification()),
                contractEventFilter.getStatuses(),
                FilterSyncStateResponse.fromDomain(contractEventFilter.getFilterSyncState()),
                EventFilterVisibilityConfigurationResponse.fromDomain(
                        contractEventFilter.getVisibilityConfiguration()),
                contractEventFilter.getContractAddress());
    }
}
