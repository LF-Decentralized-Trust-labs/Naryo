package io.naryo.infrastructure.configuration.source.env.model.filter.event.contract;

import io.naryo.application.configuration.source.model.filter.event.contract.ContractEventFilterDescriptor;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.event.EventFilterScope;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.EventFilterProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.EventSpecification;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.FilterSyncProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.visibility.EventFilterVisibilityProperties;
import jakarta.annotation.Nullable;
import lombok.Setter;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
        super(
                id,
                name,
                nodeId,
                EventFilterScope.CONTRACT,
                specification,
                statuses,
                sync,
                visibility);
        this.address = address;
    }

    @Override
    public Optional<String> getAddress() {
        return Optional.ofNullable(address);
    }
}
