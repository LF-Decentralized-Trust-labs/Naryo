package io.naryo.infrastructure.configuration.persistence.entity.broadcaster.target;

import io.naryo.application.configuration.source.model.broadcaster.target.BlockBroadcasterTargetDescriptor;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("block")
@NoArgsConstructor
public class BlockBroadcasterTargetEntity extends BroadcasterTargetEntity
        implements BlockBroadcasterTargetDescriptor {

    public BlockBroadcasterTargetEntity(String destination) {
        super(destination);
    }
}
