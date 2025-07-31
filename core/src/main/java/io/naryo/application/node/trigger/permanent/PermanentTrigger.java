package io.naryo.application.node.trigger.permanent;

import io.naryo.application.node.trigger.Trigger;
import io.naryo.domain.event.Event;
import io.reactivex.functions.Consumer;

public interface PermanentTrigger<E extends Event> extends Trigger<E> {

    void onExecute(Consumer<E> consumer);
}
