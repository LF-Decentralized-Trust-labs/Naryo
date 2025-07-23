package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import io.naryo.application.configuration.source.model.broadcaster.target.AllBroadcasterTargetDescriptor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class AllBroadcasterTargetProperties extends BroadcasterTargetProperties
        implements AllBroadcasterTargetDescriptor {

    public AllBroadcasterTargetProperties(String destination) {
        super(destination);
    }
}
