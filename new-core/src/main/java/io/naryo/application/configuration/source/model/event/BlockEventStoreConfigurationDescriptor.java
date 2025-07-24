package io.naryo.application.configuration.source.model.event;

import java.util.Optional;
import java.util.Set;

import io.naryo.domain.configuration.eventstore.EventStoreStrategy;

import static io.naryo.application.common.util.MergeUtil.mergeCollections;

public interface BlockEventStoreConfigurationDescriptor extends EventStoreConfigurationDescriptor {

    Set<? extends EventStoreTargetDescriptor> getTargets();

    void setTargets(Set<? extends EventStoreTargetDescriptor> targets);

    @Override
    default Optional<EventStoreStrategy> getStrategy() {
        return Optional.of(EventStoreStrategy.BLOCK_BASED);
    }

    @Override
    default EventStoreConfigurationDescriptor merge(EventStoreConfigurationDescriptor other) {
        EventStoreConfigurationDescriptor.super.merge(other);

        if (other instanceof BlockEventStoreConfigurationDescriptor otherBlock) {
            mergeCollections(this::setTargets, this.getTargets(), otherBlock.getTargets());
        }

        return this;
    }
}
