package io.naryo.infrastructure.configuration.source.env.model.store.event.block;

import java.util.Set;
import java.util.stream.Collectors;

import io.naryo.application.configuration.source.model.store.event.BlockEventStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.event.EventStoreTargetDescriptor;
import io.naryo.domain.configuration.store.active.feature.event.EventStoreStrategy;
import io.naryo.infrastructure.configuration.source.env.model.store.event.EventStoreConfigurationProperties;
import io.naryo.infrastructure.configuration.source.env.model.store.event.EventStoreTargetProperties;
import lombok.Getter;

@Getter
public class BlockEventStoreConfigurationProperties extends EventStoreConfigurationProperties
        implements BlockEventStoreConfigurationDescriptor {

    private Set<EventStoreTargetProperties> targets;

    public BlockEventStoreConfigurationProperties(Set<EventStoreTargetProperties> targets) {
        super(EventStoreStrategy.BLOCK_BASED);
        this.targets = targets;
    }

    @Override
    public void setTargets(Set<? extends EventStoreTargetDescriptor> targets) {
        if (targets.stream().allMatch(t -> t instanceof EventStoreTargetProperties)) {
            this.targets =
                    targets.stream()
                            .map(t -> (EventStoreTargetProperties) t)
                            .collect(Collectors.toSet());
        } else {
            throw new IllegalArgumentException("Unsupported target type: " + targets.getClass());
        }
    }
}
