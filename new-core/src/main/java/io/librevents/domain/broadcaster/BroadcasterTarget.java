package io.librevents.domain.broadcaster;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
public abstract class BroadcasterTarget {

    protected final BroadcasterTargetType type;
    protected final Destination destination;

    protected BroadcasterTarget(BroadcasterTargetType type, Destination destination) {
        Objects.requireNonNull(destination, "destination must not be null");
        this.type = type;
        this.destination = destination;
    }
}
