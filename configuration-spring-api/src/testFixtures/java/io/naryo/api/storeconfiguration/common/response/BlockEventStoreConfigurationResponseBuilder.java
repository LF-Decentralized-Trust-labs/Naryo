package io.naryo.api.storeconfiguration.common.response;

import java.util.HashSet;
import java.util.Set;

import io.naryo.api.RequestBuilder;
import io.naryo.domain.configuration.store.active.feature.event.EventStoreStrategy;
import io.naryo.domain.configuration.store.active.feature.event.block.TargetType;

public class BlockEventStoreConfigurationResponseBuilder
        implements RequestBuilder<
                BlockEventStoreConfigurationResponseBuilder, BlockEventStoreConfigurationResponse> {

    private Set<EventStoreTargetResponse> targets;

    @Override
    public BlockEventStoreConfigurationResponseBuilder self() {
        return this;
    }

    @Override
    public BlockEventStoreConfigurationResponse build() {
        return new BlockEventStoreConfigurationResponse(
                EventStoreStrategy.BLOCK_BASED, getTargets());
    }

    public BlockEventStoreConfigurationResponseBuilder withTargets(
            Set<EventStoreTargetResponse> targets) {
        this.targets = targets;
        return self();
    }

    public Set<EventStoreTargetResponse> getTargets() {
        if (this.targets != null) {
            return this.targets;
        }

        Set<EventStoreTargetResponse> defaultTargets = new HashSet<>();

        defaultTargets.add(
                new EventStoreTargetResponseBuilder().withType(TargetType.BLOCK).build());
        defaultTargets.add(
                new EventStoreTargetResponseBuilder().withType(TargetType.CONTRACT_EVENT).build());
        defaultTargets.add(
                new EventStoreTargetResponseBuilder().withType(TargetType.TRANSACTION).build());

        return defaultTargets;
    }
}
