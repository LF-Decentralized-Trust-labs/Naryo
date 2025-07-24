package io.naryo.domain.broadcaster.target;

import io.naryo.domain.DomainBuilder;
import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import io.naryo.domain.broadcaster.Destination;
import org.instancio.Instancio;

public abstract class BroadcasterTargetBuilder<
                T extends BroadcasterTargetBuilder<T, Y>, Y extends BroadcasterTarget>
        implements DomainBuilder<T, Y> {

    private BroadcasterTargetType type;
    private Destination destination;

    @Override
    public abstract T self();

    @Override
    public abstract Y build();

    public T withType(BroadcasterTargetType type) {
        this.type = type;
        return self();
    }

    public T withDestination(Destination destination) {
        this.destination = destination;
        return self();
    }

    protected Destination getDestination() {
        return this.destination == null ? Instancio.create(Destination.class) : this.destination;
    }

    protected BroadcasterTargetType getType() {
        return this.type == null ? Instancio.create(BroadcasterTargetType.class) : this.type;
    }
}
