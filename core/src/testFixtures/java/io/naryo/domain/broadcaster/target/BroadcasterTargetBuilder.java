package io.naryo.domain.broadcaster.target;

import io.naryo.domain.DomainBuilder;
import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.common.Destination;
import org.instancio.Instancio;

public abstract class BroadcasterTargetBuilder<
                T extends BroadcasterTargetBuilder<T, Y>, Y extends BroadcasterTarget>
        implements DomainBuilder<T, Y> {

    private Destination destination;

    public T withDestination(Destination destination) {
        this.destination = destination;
        return self();
    }

    protected Destination getDestination() {
        return this.destination == null ? Instancio.create(Destination.class) : this.destination;
    }
}
