package io.naryo.infrastructure.configuration.source.env.model.broadcaster.target;

import java.util.List;

import io.naryo.application.configuration.source.model.broadcaster.target.BlockBroadcasterTargetDescriptor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class BlockBroadcasterTargetProperties extends BroadcasterTargetProperties
        implements BlockBroadcasterTargetDescriptor {

    public BlockBroadcasterTargetProperties(List<String> destinations) {
        super(destinations);
    }
}
