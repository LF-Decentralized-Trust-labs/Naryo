package io.naryo.infrastructure.configuration.persistence.entity.store.event;

import java.util.Set;

import io.naryo.application.configuration.source.model.store.event.BlockEventStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.event.EventStoreTargetDescriptor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("block_event")
@Getter
@NoArgsConstructor
public final class BlockStoreConfigurationEntity extends EventStoreConfigurationEntity
        implements BlockEventStoreConfigurationDescriptor {

    private @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) @JoinColumn(
            name = "event_store_id") Set<EventStoreTargetEntity> targets;

    public BlockStoreConfigurationEntity(Set<EventStoreTargetEntity> targets) {
        super();
        this.targets = targets;
    }

    @Override
    public void setTargets(Set<? extends EventStoreTargetDescriptor> targets) {
        if (targets.stream().allMatch(t -> t instanceof EventStoreTargetEntity)) {
            this.targets =
                    targets.stream()
                            .map(t -> (EventStoreTargetEntity) t)
                            .collect(java.util.HashSet::new, Set::add, Set::addAll);
        } else {
            throw new IllegalArgumentException("Unsupported target type: " + targets.getClass());
        }
    }
}
