package io.librevents.application.node.trigger.permanent;

import java.util.List;

import io.librevents.application.event.store.EventStore;
import io.librevents.domain.event.Event;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class EventStoreBroadcasterPermanentTrigger implements PermanentTrigger<Event> {

    private final List<EventStore<Event>> eventStores;
    private Consumer<Event> consumer;

    public EventStoreBroadcasterPermanentTrigger(List<EventStore<Event>> eventStores) {
        this.eventStores = eventStores;
    }

    @Override
    public void onExecute(Consumer<Event> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void trigger(Event event) {
        this.eventStores.stream()
                .filter(eventStore -> eventStore.supports(event))
                .forEach(
                        eventStore -> {
                            try {
                                eventStore.save(event);
                            } catch (Exception e) {
                                log.error("Error while saving event to event store", e);
                            }
                        });
        if (consumer != null) {
            try {
                consumer.accept(event);
            } catch (Exception e) {
                log.error("Error while consumer execution", e);
            }
        }
    }

    @Override
    public boolean supports(Event event) {
        return true;
    }
}
