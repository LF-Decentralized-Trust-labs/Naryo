package io.naryo.api.filter.update.model;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.ContractEventFilter;
import io.naryo.domain.filter.event.EventFilterSpecification;
import io.naryo.domain.filter.event.EventFilterVisibilityConfiguration;
import io.naryo.domain.filter.event.FilterSyncState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Valid
@Getter
public final class UpdateContractEventFilterRequest extends UpdateFilterRequest {

    @Schema(example = "CONTRACT-EVENT", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String type = "CONTRACT-EVENT";

    @NotNull
    private final EventFilterSpecification specification;

    private final Set<ContractEventStatus> statuses;

    @NotNull
    private final FilterSyncState filterSyncState;

    private final EventFilterVisibilityConfiguration visibilityConfiguration;

    @NotBlank
    private final String contractAddress;

    public UpdateContractEventFilterRequest(
        String name,
        UUID nodeId,
        EventFilterSpecification specification,
        Set<ContractEventStatus> statuses,
        FilterSyncState filterSyncState,
        EventFilterVisibilityConfiguration visibilityConfiguration,
        String contractAddress,
        String prevItemHash) {
        super( name, nodeId, prevItemHash);
        this.specification = specification;
        this.statuses = statuses;
        this.filterSyncState = filterSyncState;
        this.visibilityConfiguration = visibilityConfiguration;
        this.contractAddress = contractAddress;
    }

    @Override
    public ContractEventFilter toDomain( UUID idFromPath) {
        return new ContractEventFilter(
            idFromPath,
            new FilterName(name),
            nodeId,
            specification,
            statuses,
            filterSyncState,
            visibilityConfiguration,
            contractAddress);
    }
}
