package io.naryo.domain.broadcaster;

import java.util.List;
import java.util.Objects;

import io.naryo.domain.common.Destination;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class BroadcasterTarget {

    protected final BroadcasterTargetType type;
    protected final List<Destination> destinations;

    protected BroadcasterTarget(BroadcasterTargetType type, List<Destination> destinations) {
        Objects.requireNonNull(destinations, "destination must not be null");
        if (destinations.isEmpty()) {
            throw new IllegalArgumentException("destination must not be empty");
        }
        this.type = type;
        this.destinations = destinations;
    }
}
