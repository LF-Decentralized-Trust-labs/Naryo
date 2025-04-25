package io.librevents.application.node.trigger.disposable;

import io.librevents.application.node.trigger.Trigger;
import io.librevents.domain.event.Event;
import io.reactivex.functions.Consumer;

public interface DisposableTrigger<E extends Event> extends Trigger<E> {

    void onDispose(Consumer<E> consumer);

}
