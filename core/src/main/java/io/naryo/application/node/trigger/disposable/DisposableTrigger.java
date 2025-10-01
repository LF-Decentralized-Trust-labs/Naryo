package io.naryo.application.node.trigger.disposable;

import io.naryo.application.node.trigger.Trigger;
import io.naryo.domain.event.Event;
import io.reactivex.functions.Consumer;

public interface DisposableTrigger<E extends Event<?>> extends Trigger<E> {

    void onDispose(Consumer<E> consumer);
}
