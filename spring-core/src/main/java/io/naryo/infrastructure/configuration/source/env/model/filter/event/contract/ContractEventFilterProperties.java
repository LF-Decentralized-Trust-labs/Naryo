package io.naryo.infrastructure.configuration.source.env.model.filter.event.contract;

import io.naryo.application.configuration.source.model.filter.event.contract.ContractEventFilterDescriptor;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.event.EventFilterScope;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.EventFilterProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.EventSpecification;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.FilterSyncProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.visibility.EventFilterVisibilityProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public final class ContractEventFilterProperties extends EventFilterProperties
        implements ContractEventFilterDescriptor {

    private Optional<String> address;

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
        this.address = Optional.ofNullable(address);
    }

}
