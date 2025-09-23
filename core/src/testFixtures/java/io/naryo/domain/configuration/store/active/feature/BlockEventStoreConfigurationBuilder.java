package io.naryo.domain.configuration.store.active.feature;

import java.util.Set;

import io.naryo.domain.common.Destination;
import io.naryo.domain.configuration.store.active.feature.event.block.BlockEventStoreConfiguration;
import io.naryo.domain.configuration.store.active.feature.event.block.EventStoreTarget;
import io.naryo.domain.configuration.store.active.feature.event.block.TargetType;
import org.instancio.Instancio;

public final class BlockEventStoreConfigurationBuilder
        extends EventStoreFeatureConfigurationBuilder<
                BlockEventStoreConfigurationBuilder, BlockEventStoreConfiguration> {

    private Set<EventStoreTarget> targets;

    @Override
    public BlockEventStoreConfigurationBuilder self() {
        return this;
    }

    @Override
    public BlockEventStoreConfiguration build() {
        return new BlockEventStoreConfiguration(getTargets());
    }

    public BlockEventStoreConfigurationBuilder targets(Set<EventStoreTarget> targets) {
        this.targets = targets;
        return self();
    }

    public Set<EventStoreTarget> getTargets() {
        return this.targets == null
                ? Set.of(
                        new EventStoreTarget(TargetType.BLOCK, Instancio.create(Destination.class)),
                        new EventStoreTarget(
                                TargetType.TRANSACTION, Instancio.create(Destination.class)),
                        new EventStoreTarget(
                                TargetType.CONTRACT_EVENT, Instancio.create(Destination.class)))
                : this.targets;
    }
}
