package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import java.util.List;
import java.util.Set;

import io.naryo.application.configuration.source.model.broadcaster.target.BlockBroadcasterTargetDescriptor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class BlockBroadcasterTargetProperties extends BroadcasterTargetProperties
        implements BlockBroadcasterTargetDescriptor {

    public BlockBroadcasterTargetProperties(Set<String> destinations) {
        super(destinations);
    }
}
