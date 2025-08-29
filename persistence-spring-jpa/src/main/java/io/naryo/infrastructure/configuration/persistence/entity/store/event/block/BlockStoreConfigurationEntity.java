package io.naryo.infrastructure.configuration.persistence.entity.store.event.block;

import java.util.Set;

import io.naryo.application.configuration.source.model.store.event.BlockEventStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.event.EventStoreTargetDescriptor;
import io.naryo.domain.configuration.store.active.feature.event.EventStoreStrategy;
import io.naryo.infrastructure.configuration.persistence.entity.store.event.EventStoreConfigurationEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("block")
@Getter
@NoArgsConstructor
public final class BlockStoreConfigurationEntity extends EventStoreConfigurationEntity
        implements BlockEventStoreConfigurationDescriptor {

    private @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) @JoinColumn(
            name = "block_store_id") Set<EventStoreTargetEntity> targets;

    public BlockStoreConfigurationEntity(Set<EventStoreTargetEntity> targets) {
        super(EventStoreStrategy.BLOCK_BASED);
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
