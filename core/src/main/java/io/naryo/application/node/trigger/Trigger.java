package io.naryo.application.node.trigger;

import io.naryo.domain.event.Event;

public interface Trigger<E extends Event<?>> {

    void trigger(E event);

    boolean supports(Event<?> event);
}
