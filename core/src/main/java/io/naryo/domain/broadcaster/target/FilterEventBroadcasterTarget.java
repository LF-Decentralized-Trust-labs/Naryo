package io.naryo.domain.broadcaster.target;

import java.util.Objects;
import java.util.UUID;

import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import io.naryo.domain.broadcaster.Destination;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class FilterEventBroadcasterTarget extends BroadcasterTarget {

    private final UUID filterId;

    public FilterEventBroadcasterTarget(Destination destination, UUID filterId) {
        super(BroadcasterTargetType.FILTER, destination);
        Objects.requireNonNull(filterId, "filterId cannot be null");
        this.filterId = filterId;
    }
}
