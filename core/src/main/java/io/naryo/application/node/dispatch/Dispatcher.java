package io.naryo.application.node.dispatch;

import io.naryo.application.node.trigger.Trigger;
import io.naryo.domain.event.Event;

public interface Dispatcher {

    void dispatch(Event<?> event);

    void addTrigger(Trigger<?> trigger);

    void removeTrigger(Trigger<?> trigger);
}
