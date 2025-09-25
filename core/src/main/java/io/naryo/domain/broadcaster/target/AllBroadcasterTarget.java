package io.naryo.domain.broadcaster.target;

import java.util.List;

import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import io.naryo.domain.common.Destination;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public final class AllBroadcasterTarget extends BroadcasterTarget {

    public AllBroadcasterTarget(List<Destination> destinations) {
        super(BroadcasterTargetType.ALL, destinations);
    }
}
