package io.naryo.infrastructure.configuration.source.env.model.filter.event.contract;

import java.util.List;
import java.util.UUID;

import io.naryo.application.configuration.source.model.filter.event.contract.ContractEventFilterDescriptor;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.event.EventFilterScope;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.EventFilterProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.EventSpecification;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.FilterSyncProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.visibility.EventFilterVisibilityProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public final class ContractEventFilterProperties extends EventFilterProperties
        implements ContractEventFilterDescriptor {

    private @Getter @Setter @NotBlank String address;

    public ContractEventFilterProperties(
            UUID id,
            String name,
            UUID nodeId,
            EventSpecification specification,
            List<ContractEventStatus> statuses,
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
}
