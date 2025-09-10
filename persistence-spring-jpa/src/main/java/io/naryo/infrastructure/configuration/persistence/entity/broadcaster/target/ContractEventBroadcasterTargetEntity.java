package io.naryo.infrastructure.configuration.persistence.entity.broadcaster.target;

import java.util.List;

import io.naryo.application.configuration.source.model.broadcaster.target.ContractEventBroadcasterTargetDescriptor;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("contract_event")
@NoArgsConstructor
public class ContractEventBroadcasterTargetEntity extends BroadcasterTargetEntity
        implements ContractEventBroadcasterTargetDescriptor {

    public ContractEventBroadcasterTargetEntity(List<String> destinations) {
        super(destinations);
    }
}
