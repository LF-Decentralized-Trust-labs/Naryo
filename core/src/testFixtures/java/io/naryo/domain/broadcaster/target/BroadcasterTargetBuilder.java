package io.naryo.domain.broadcaster.target;

import java.util.ArrayList;
import java.util.List;

import io.naryo.domain.DomainBuilder;
import io.naryo.domain.broadcaster.BroadcasterTarget;
import io.naryo.domain.common.Destination;
import org.instancio.Instancio;

public abstract class BroadcasterTargetBuilder<
                T extends BroadcasterTargetBuilder<T, Y>, Y extends BroadcasterTarget>
        implements DomainBuilder<T, Y> {

    private List<Destination> destinations;

    public T withDestination(Destination destination) {
        this.destinations = new ArrayList<>(List.of(destination));
        return self();
    }

    protected List<Destination> getDestinations() {
        return this.destinations == null
                ? new ArrayList<>(List.of(Instancio.create(Destination.class)))
                : this.destinations;
    }
}
