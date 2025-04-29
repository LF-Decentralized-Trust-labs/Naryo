package io.librevents.application.node.dispatch.block;

import java.util.HashSet;
import java.util.Set;

import io.librevents.application.node.dispatch.Dispatcher;
import io.librevents.application.node.trigger.Trigger;
import io.librevents.application.node.trigger.disposable.DisposableTrigger;
import io.librevents.application.node.trigger.permanent.PermanentTrigger;
import io.librevents.domain.event.Event;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class BlockDispatcher implements Dispatcher {

    @Getter private final Set<Trigger<?>> triggers;

    public BlockDispatcher(Set<Trigger<?>> triggers) {
        this.triggers = new HashSet<>(triggers);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void dispatch(Event event) {
        for (Trigger<?> trigger : new HashSet<>(triggers)) {
            try {
                if (trigger.supports(event)) {
                    if (trigger instanceof DisposableTrigger) {
                        ((DisposableTrigger<?>) trigger)
                                .onDispose(
                                        ignored -> {
                                            log.debug(
                                                    "Trigger {} disposed",
                                                    trigger.getClass().getSimpleName());
                                            triggers.remove(trigger);
                                        });
                    } else {
                        ((PermanentTrigger<?>) trigger)
                                .onExecute(
                                        ignore -> {
                                            log.debug(
                                                    "Trigger {} executed",
                                                    trigger.getClass().getSimpleName());
                                        });
                    }

                    Trigger<Event> typedTrigger = (Trigger<Event>) trigger;
                    typedTrigger.trigger(event);
                }
            } catch (Exception e) {
                log.error(
                        "Error while dispatching event to trigger {}: {}",
                        trigger.getClass().getSimpleName(),
                        e.getMessage(),
                        e);
            }
        }
    }

    @Override
    public void addTrigger(Trigger<?> trigger) {
        triggers.add(trigger);
    }

    @Override
    public void removeTrigger(Trigger<?> trigger) {
        triggers.remove(trigger);
    }
}
