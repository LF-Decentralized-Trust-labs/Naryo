package io.librevents.application.event.store;

import io.librevents.domain.event.Event;

public interface EventStore<E extends Event> {

    void save(E event);

    boolean supports(Event event);
}
