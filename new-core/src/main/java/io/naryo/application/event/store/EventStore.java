package io.naryo.application.event.store;

import io.naryo.domain.event.Event;

public interface EventStore<E extends Event> {

    void save(E event);

    boolean supports(Event event);
}
