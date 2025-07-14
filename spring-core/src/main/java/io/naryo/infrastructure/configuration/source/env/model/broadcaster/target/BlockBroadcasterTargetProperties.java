package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import io.naryo.application.configuration.source.model.broadcaster.target.BlockBroadcasterTargetDescriptor;
import io.naryo.domain.broadcaster.BroadcasterTargetType;

public final class BlockBroadcasterTargetProperties extends BroadcasterTargetProperties
        implements BlockBroadcasterTargetDescriptor {

    public BlockBroadcasterTargetProperties(String destination) {
        super(BroadcasterTargetType.BLOCK, destination);
    }

    public BlockBroadcasterTargetProperties() {
        super(BroadcasterTargetType.BLOCK, null);
    }
}
