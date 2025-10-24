package io.naryo.api.filter.common.request;

import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.GlobalEventFilter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Getter;

@Schema(description = "Global event filter request")
@Valid
@Getter
public final class GlobalEventFilterRequest extends EventFilterRequest {

    public GlobalEventFilterRequest(
            String name,
            UUID nodeId,
            EventFilterSpecificationRequest specification,
            Set<ContractEventStatus> statuses,
            FilterSyncStateRequest filterSyncState,
            EventFilterVisibilityConfigurationRequest visibilityConfiguration) {
        super(name, nodeId, specification, statuses, filterSyncState, visibilityConfiguration);
    }

    @Override
    public GlobalEventFilter toDomain(UUID id) {
        return new GlobalEventFilter(
                id,
                new FilterName(name),
                this.nodeId,
                this.specification.toDomain(),
                this.statuses,
                this.filterSyncState.toDomain(),
                this.visibilityConfiguration != null
                        ? this.visibilityConfiguration.toDomain()
                        : null);
    }
}
