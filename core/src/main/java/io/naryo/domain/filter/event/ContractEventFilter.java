package io.naryo.domain.filter.event;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ContractEventFilter extends EventFilter {

    private final String contractAddress;

    public ContractEventFilter(
            UUID id,
            FilterName name,
            UUID nodeId,
            EventFilterSpecification specification,
            Set<ContractEventStatus> statuses,
            FilterSyncState filterSyncState,
            EventFilterVisibilityConfiguration visibilityConfiguration,
            String contractAddress) {
        super(
                id,
                name,
                nodeId,
                EventFilterScope.CONTRACT,
                specification,
                statuses,
                filterSyncState,
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
            FilterSyncState filterSyncState,
            String contractAddress) {
        super(
                id,
                name,
                nodeId,
                EventFilterScope.CONTRACT,
                specification,
                statuses,
                filterSyncState,
                EventFilterVisibilityConfiguration.visible());
        Objects.requireNonNull(contractAddress, "Contract address cannot be null");
        if (contractAddress.isEmpty()) {
            throw new IllegalArgumentException("Contract address cannot be empty");
        }
        this.contractAddress = contractAddress;
    }
}
