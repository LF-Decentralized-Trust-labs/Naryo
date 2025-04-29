package io.librevents.application.node.dispatch;

import io.librevents.application.node.trigger.Trigger;
import io.librevents.domain.event.Event;

public interface Dispatcher {

    void dispatch(Event event);

    void addTrigger(Trigger<?> trigger);

    void removeTrigger(Trigger<?> trigger);
}
