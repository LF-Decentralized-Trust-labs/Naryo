package io.naryo.api.storeconfiguration.common;

import io.naryo.api.RequestBuilder;
import io.naryo.api.storeconfiguration.common.request.EventStoreTargetRequest;
import io.naryo.domain.configuration.store.active.feature.event.block.TargetType;
import org.instancio.Instancio;

public class EventStoreTargetRequestBuilder
        implements RequestBuilder<EventStoreTargetRequestBuilder, EventStoreTargetRequest> {

    private TargetType type;
    private String destination;

    @Override
    public EventStoreTargetRequestBuilder self() {
        return this;
    }

    @Override
    public EventStoreTargetRequest build() {
        return new EventStoreTargetRequest(getType(), getDestination());
    }

    public EventStoreTargetRequestBuilder withType(TargetType type) {
        this.type = type;
        return self();
    }

    public TargetType getType() {
        return this.type == null ? Instancio.create(TargetType.class) : this.type;
    }

    public EventStoreTargetRequestBuilder withDestination(String destination) {
        this.destination = destination;
        return self();
    }

    public String getDestination() {
        return this.destination == null ? Instancio.create(String.class) : this.destination;
    }
}
