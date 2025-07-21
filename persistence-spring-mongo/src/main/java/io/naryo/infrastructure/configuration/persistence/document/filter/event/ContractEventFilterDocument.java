package io.naryo.infrastructure.configuration.persistence.document.filter.event;

import io.naryo.application.configuration.source.model.filter.event.contract.ContractEventFilterDescriptor;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.event.EventFilterScope;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.sync.FilterSyncDocument;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

import java.util.Optional;
import java.util.Set;

@TypeAlias("contract_event_filter")
@Setter
public class ContractEventFilterDocument extends EventFilterDocument implements ContractEventFilterDescriptor {

    @NotNull
    private String address;

    public ContractEventFilterDocument(String id,
                                       String name,
                                       String nodeId,
                                       EventFilterScope scope,
                                       EventSpecificationDocument specification,
                                       Set<ContractEventStatus> statuses,
                                       FilterSyncDocument sync,
                                       FilterVisibilityDocument visibility,
                                       String address) {
        super(id, name, nodeId, scope, specification, statuses, sync, visibility);
        this.address = address;
    }

    @Override
    public Optional<String> getAddress() {
        return Optional.ofNullable(address);
    }

}
