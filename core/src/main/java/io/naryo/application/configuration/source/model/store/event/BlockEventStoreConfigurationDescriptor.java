package io.naryo.application.configuration.source.model.store.event;

import java.util.Set;

import io.naryo.application.configuration.source.model.store.StoreFeatureConfigurationDescriptor;
import io.naryo.domain.configuration.store.active.feature.event.EventStoreStrategy;

import static io.naryo.application.common.util.MergeUtil.mergeCollections;

public interface BlockEventStoreConfigurationDescriptor extends EventStoreConfigurationDescriptor {

    Set<? extends EventStoreTargetDescriptor> getTargets();

    void setTargets(Set<? extends EventStoreTargetDescriptor> targets);

    @Override
    default EventStoreStrategy getStrategy() {
        return EventStoreStrategy.BLOCK_BASED;
    }

    @Override
    default StoreFeatureConfigurationDescriptor merge(StoreFeatureConfigurationDescriptor other) {
        if (!(other
                instanceof
                BlockEventStoreConfigurationDescriptor otherBlockEventStoreConfiguration)) {
            return this;
        }
        mergeCollections(
                this::setTargets,
                this.getTargets(),
                otherBlockEventStoreConfiguration.getTargets());
        return this;
    }
}
