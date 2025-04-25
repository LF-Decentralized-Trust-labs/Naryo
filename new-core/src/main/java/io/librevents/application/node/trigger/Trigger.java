package io.librevents.application.node.trigger;

import io.librevents.domain.event.Event;

public interface Trigger<E extends Event> {

    void trigger(E event);

    boolean supports(Event event);

}
