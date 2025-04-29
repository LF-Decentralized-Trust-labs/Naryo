package io.librevents.application.node.trigger.permanent;

import io.librevents.application.node.trigger.Trigger;
import io.librevents.domain.event.Event;
import io.reactivex.functions.Consumer;

public interface PermanentTrigger<E extends Event> extends Trigger<E> {

    void onExecute(Consumer<E> consumer);
}
