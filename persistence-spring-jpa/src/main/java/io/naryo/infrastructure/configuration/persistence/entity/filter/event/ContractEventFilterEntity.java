package io.naryo.infrastructure.configuration.persistence.entity.filter.event;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import io.naryo.application.configuration.source.model.filter.event.contract.ContractEventFilterDescriptor;
import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.infrastructure.configuration.persistence.entity.filter.event.sync.FilterSyncEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("event_contract")
@Setter
@NoArgsConstructor
public class ContractEventFilterEntity extends EventFilterEntity
        implements ContractEventFilterDescriptor {

    private @Column(name = "address") @Nullable String address;

    public ContractEventFilterEntity(
            UUID id,
            String name,
            UUID nodeId,
            EventSpecification specification,
            Set<ContractEventStatus> statuses,
            FilterSyncEntity sync,
            FilterVisibility visibility,
            String address) {
        super(id, name, nodeId, specification, statuses, sync, visibility);
        this.address = address;
    }

    @Override
    public Optional<String> getAddress() {
        return Optional.ofNullable(address);
    }
}
