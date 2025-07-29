package io.naryo.infrastructure.configuration.persistence.entity.broadcaster.target;

import io.naryo.application.configuration.source.model.broadcaster.target.TransactionBroadcasterTargetDescriptor;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("transaction")
@NoArgsConstructor
public class TransactionBroadcasterTargetEntity extends BroadcasterTargetEntity
        implements TransactionBroadcasterTargetDescriptor {

    public TransactionBroadcasterTargetEntity(String destination) {
        super(destination);
    }
}
