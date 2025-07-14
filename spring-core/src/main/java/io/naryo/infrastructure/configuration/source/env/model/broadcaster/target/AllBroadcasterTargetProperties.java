package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import io.naryo.application.configuration.source.model.broadcaster.target.AllBroadcasterTargetDescriptor;
import io.naryo.domain.broadcaster.BroadcasterTargetType;

public final class AllBroadcasterTargetProperties extends BroadcasterTargetProperties
        implements AllBroadcasterTargetDescriptor {

    public AllBroadcasterTargetProperties(String destination) {
        super(BroadcasterTargetType.ALL, destination);
    }

    public AllBroadcasterTargetProperties() {
        super(BroadcasterTargetType.ALL, null);
    }
}
