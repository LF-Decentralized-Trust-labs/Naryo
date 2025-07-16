package io.naryo.infrastructure.configuration.persistence.document.filter.event;

import io.naryo.application.configuration.source.model.filter.event.contract.ContractEventFilterDescriptor;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.event.EventFilterScope;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.sync.FilterSyncDocument;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;

@TypeAlias("contract_event_filter")
@Getter
@Setter
public class ContractEventFilterDocument extends EventFilterDocument implements ContractEventFilterDescriptor {

    @NotNull
    private String address;

    public ContractEventFilterDocument(String id,
                                       String name,
                                       String nodeId,
                                       EventFilterScope scope,
                                       EventFilterSpecificationDocument specification,
                                       List<ContractEventStatus> statuses,
                                       @Nullable FilterSyncDocument sync,
                                       EventFilterVisibilityConfigurationDocument visibilityConfiguration,
                                       String address) {
        super(id, name, nodeId, scope, specification, statuses, sync, visibilityConfiguration);
        this.address = address;
    }

}
