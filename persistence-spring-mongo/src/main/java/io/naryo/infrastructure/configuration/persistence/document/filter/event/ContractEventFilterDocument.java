package io.naryo.infrastructure.configuration.persistence.document.filter.event;

import java.util.Optional;
import java.util.Set;

import io.naryo.application.configuration.source.model.filter.event.contract.ContractEventFilterDescriptor;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.infrastructure.configuration.persistence.document.filter.event.sync.FilterSyncDocument;
import jakarta.annotation.Nullable;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("contract_event_filter")
@Setter
public class ContractEventFilterDocument extends EventFilterDocument
        implements ContractEventFilterDescriptor {

    private @Nullable String address;

    public ContractEventFilterDocument(
            String id,
            String name,
            String nodeId,
            EventSpecificationDocument specification,
            Set<ContractEventStatus> statuses,
            FilterSyncDocument sync,
            FilterVisibilityDocument visibility,
            String address) {
        super(id, name, nodeId, specification, statuses, sync, visibility);
        this.address = address;
    }

    @Override
    public Optional<String> getAddress() {
        return Optional.ofNullable(address);
    }
}
