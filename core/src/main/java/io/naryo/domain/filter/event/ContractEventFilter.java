package io.naryo.domain.filter.event;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ContractEventFilter extends EventFilter {

    private final String contractAddress;

    public ContractEventFilter(
            UUID id,
            FilterName name,
            UUID nodeId,
            EventFilterSpecification specification,
            Set<ContractEventStatus> statuses,
            SyncState syncState,
            EventFilterVisibilityConfiguration visibilityConfiguration,
            String contractAddress) {
        super(
                id,
                name,
                nodeId,
                EventFilterScope.CONTRACT,
                specification,
                statuses,
                syncState,
                visibilityConfiguration);
        Objects.requireNonNull(contractAddress, "Contract address cannot be null");
        if (contractAddress.isEmpty()) {
            throw new IllegalArgumentException("Contract address cannot be empty");
        }
        this.contractAddress = contractAddress;
    }

    public ContractEventFilter(
            UUID id,
            FilterName name,
            UUID nodeId,
            EventFilterSpecification specification,
            Set<ContractEventStatus> statuses,
            SyncState syncState,
            String contractAddress) {
        super(
                id,
                name,
                nodeId,
                EventFilterScope.CONTRACT,
                specification,
                statuses,
                syncState,
                EventFilterVisibilityConfiguration.visible());
        Objects.requireNonNull(contractAddress, "Contract address cannot be null");
        if (contractAddress.isEmpty()) {
            throw new IllegalArgumentException("Contract address cannot be empty");
        }
        this.contractAddress = contractAddress;
    }
}
