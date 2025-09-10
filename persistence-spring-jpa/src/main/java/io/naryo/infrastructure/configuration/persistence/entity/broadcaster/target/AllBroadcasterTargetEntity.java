package io.naryo.infrastructure.configuration.persistence.entity.broadcaster.target;

import java.util.List;

import io.naryo.application.configuration.source.model.broadcaster.target.AllBroadcasterTargetDescriptor;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("all")
@NoArgsConstructor
public class AllBroadcasterTargetEntity extends BroadcasterTargetEntity
        implements AllBroadcasterTargetDescriptor {

    public AllBroadcasterTargetEntity(List<String> destinations) {
        super(destinations);
    }
}
