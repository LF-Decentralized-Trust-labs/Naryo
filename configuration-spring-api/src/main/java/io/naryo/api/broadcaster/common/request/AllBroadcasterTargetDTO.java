package io.naryo.api.broadcaster.common.request;

import java.util.List;

import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import io.naryo.domain.broadcaster.target.AllBroadcasterTarget;
import io.naryo.domain.common.Destination;
import jakarta.validation.constraints.NotEmpty;

public final class AllBroadcasterTargetDTO extends BroadcasterTargetDTO {

    public AllBroadcasterTargetDTO(@NotEmpty List<String> destinations) {
        super(BroadcasterTargetType.ALL, destinations);
    }

    @Override
    public BroadcasterTarget toDomain() {
        return new AllBroadcasterTarget(getDestinations().stream().map(Destination::new).toList());
    }
}
