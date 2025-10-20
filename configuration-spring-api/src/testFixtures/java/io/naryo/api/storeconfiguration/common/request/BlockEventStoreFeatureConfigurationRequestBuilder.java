package io.naryo.api.storeconfiguration.common.request;

import java.util.HashSet;
import java.util.Set;

import io.naryo.api.RequestBuilder;
import io.naryo.domain.configuration.store.active.feature.event.block.TargetType;

public class BlockEventStoreFeatureConfigurationRequestBuilder
        implements RequestBuilder<
                BlockEventStoreFeatureConfigurationRequestBuilder,
                BlockEventStoreFeatureConfigurationRequest> {

    private Set<EventStoreTargetRequest> targets;

    @Override
    public BlockEventStoreFeatureConfigurationRequestBuilder self() {
        return this;
    }

    @Override
    public BlockEventStoreFeatureConfigurationRequest build() {
        return new BlockEventStoreFeatureConfigurationRequest(getTargets());
    }

    public BlockEventStoreFeatureConfigurationRequestBuilder withTargets(
            Set<EventStoreTargetRequest> targets) {
        this.targets = targets;
        return self();
    }

    public Set<EventStoreTargetRequest> getTargets() {
        if (this.targets != null) {
            return this.targets;
        }

        Set<EventStoreTargetRequest> defaultTargets = new HashSet<>();

        defaultTargets.add(new EventStoreTargetRequestBuilder().withType(TargetType.BLOCK).build());
        defaultTargets.add(
                new EventStoreTargetRequestBuilder().withType(TargetType.CONTRACT_EVENT).build());
        defaultTargets.add(
                new EventStoreTargetRequestBuilder().withType(TargetType.TRANSACTION).build());

        return defaultTargets;
    }
}
