package io.naryo.api.storeconfiguration.common.response;

import io.naryo.api.RequestBuilder;
import io.naryo.domain.configuration.store.active.feature.event.block.TargetType;
import org.instancio.Instancio;

public class EventStoreTargetResponseBuilder
        implements RequestBuilder<EventStoreTargetResponseBuilder, EventStoreTargetResponse> {

    private TargetType type;
    private String destination;

    @Override
    public EventStoreTargetResponseBuilder self() {
        return this;
    }

    @Override
    public EventStoreTargetResponse build() {
        return new EventStoreTargetResponse(getType(), getDestination());
    }

    public EventStoreTargetResponseBuilder withType(TargetType type) {
        this.type = type;
        return self();
    }

    public TargetType getType() {
        return this.type == null ? Instancio.create(TargetType.class) : this.type;
    }

    public EventStoreTargetResponseBuilder withDestination(String destination) {
        this.destination = destination;
        return self();
    }

    public String getDestination() {
        return this.destination == null ? Instancio.create(String.class) : this.destination;
    }
}
