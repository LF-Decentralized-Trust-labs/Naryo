package io.naryo.api.filter.create.model;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Getter
public final class CreateContractEventFilterRequest extends CreateFilterRequest {

    @Schema(example = "CONTRACT-EVENT")
    private final String type = "CONTRACT-EVENT";

    @NotNull
    private final EventFilterSpecification specification;

    private final Set<ContractEventStatus> statuses;

    @NotNull
    private final FilterSyncState filterSyncState;

    private final String contractAddress;



    public CreateContractEventFilterRequest(String name,
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
    public Filter toDomain() {
        return new ContractEventFilter(
            UUID.randomUUID(),
            new FilterName(this.name),
            this.nodeId,
            this.specification,
            this.statuses,
            this.filterSyncState,
            this.contractAddress
        );
    }
}
