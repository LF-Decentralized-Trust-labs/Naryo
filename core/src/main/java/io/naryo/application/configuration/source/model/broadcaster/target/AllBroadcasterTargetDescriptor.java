package io.naryo.application.configuration.source.model.broadcaster.target;

import io.naryo.domain.broadcaster.BroadcasterTargetType;

public interface AllBroadcasterTargetDescriptor extends BroadcasterTargetDescriptor {

    @Override
    default BroadcasterTargetType getType() {
        return BroadcasterTargetType.ALL;
    }
}
