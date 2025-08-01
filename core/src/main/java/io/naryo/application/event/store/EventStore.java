package io.naryo.application.event.store;

import io.naryo.domain.configuration.eventstore.EventStoreConfiguration;
import io.naryo.domain.event.Event;

public interface EventStore<E extends Event, C extends EventStoreConfiguration> {

    void save(E event, C configuration);

    boolean supports(Event event, EventStoreConfiguration configuration);
}
