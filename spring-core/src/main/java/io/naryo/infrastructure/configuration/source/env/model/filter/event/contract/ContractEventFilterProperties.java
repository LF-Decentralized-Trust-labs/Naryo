package io.naryo.infrastructure.configuration.source.env.model.filter.event.contract;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import io.naryo.application.configuration.source.model.filter.event.contract.ContractEventFilterDescriptor;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.EventFilterProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.EventSpecification;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.FilterSyncProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.visibility.EventFilterVisibilityProperties;
import jakarta.annotation.Nullable;
import lombok.Setter;

@Setter
public final class ContractEventFilterProperties extends EventFilterProperties
        implements ContractEventFilterDescriptor {

    private @Nullable String address;

    public ContractEventFilterProperties(
            UUID id,
            String name,
            UUID nodeId,
            EventSpecification specification,
            Set<ContractEventStatus> statuses,
            FilterSyncProperties sync,
            EventFilterVisibilityProperties visibility,
            String address) {
        super(id, name, nodeId, specification, statuses, sync, visibility);
        this.address = address;
    }

    @Override
    public Optional<String> getAddress() {
        return Optional.ofNullable(address);
    }
}
