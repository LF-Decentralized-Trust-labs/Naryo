package io.naryo.application.configuration.source.model.event;

import java.util.Set;

import io.naryo.domain.configuration.eventstore.EventStoreStrategy;

import static io.naryo.application.common.util.MergeUtil.mergeCollections;

public interface BlockEventStoreConfigurationDescriptor extends EventStoreConfigurationDescriptor {

    Set<? extends EventStoreTargetDescriptor> getTargets();

    void setTargets(Set<? extends EventStoreTargetDescriptor> targets);

    @Override
    default EventStoreStrategy getStrategy() {
        return EventStoreStrategy.BLOCK_BASED;
    }

    @Override
    default EventStoreConfigurationDescriptor merge(EventStoreConfigurationDescriptor other) {
        if (!(other
                instanceof
                BlockEventStoreConfigurationDescriptor otherBlockEventStoreConfiguration)) {
            return this;
        }
        mergeCollections(
                this::setTargets,
                this.getTargets(),
                otherBlockEventStoreConfiguration.getTargets());
        return EventStoreConfigurationDescriptor.super.merge(other);
    }
}
