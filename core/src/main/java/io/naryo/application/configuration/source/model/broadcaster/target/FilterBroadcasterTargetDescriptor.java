package io.naryo.application.configuration.source.model.broadcaster.target;

import java.util.UUID;

import io.naryo.domain.broadcaster.BroadcasterTargetType;

public interface FilterBroadcasterTargetDescriptor extends BroadcasterTargetDescriptor {

    UUID getFilterId();

    @Override
    default BroadcasterTargetType getType() {
        return BroadcasterTargetType.FILTER;
    }

    @Override
    default BroadcasterTargetDescriptor merge(BroadcasterTargetDescriptor other) {
        if (!(other instanceof FilterBroadcasterTargetDescriptor otherFilterBroadcasterTarget)) {
            return this;
        }

        return BroadcasterTargetDescriptor.super.merge(otherFilterBroadcasterTarget);
    }
}
