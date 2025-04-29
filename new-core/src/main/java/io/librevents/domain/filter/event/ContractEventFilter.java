package io.librevents.domain.filter.event;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import io.librevents.domain.common.ContractEventStatus;
import io.librevents.domain.filter.FilterName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class ContractEventFilter extends EventFilter {

    private final String contractAddress;

    public ContractEventFilter(
            UUID id,
            FilterName name,
            UUID nodeId,
            EventFilterSpecification specification,
            List<ContractEventStatus> statuses,
            SyncState syncState,
            String contractAddress) {
        super(id, name, nodeId, EventFilterScope.CONTRACT, specification, statuses, syncState);
        Objects.requireNonNull(contractAddress, "Contract address cannot be null");
        if (contractAddress.isEmpty()) {
            throw new IllegalArgumentException("Contract address cannot be empty");
        }
        this.contractAddress = contractAddress;
    }
}
