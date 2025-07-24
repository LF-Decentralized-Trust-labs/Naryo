package io.naryo.application.node.trigger.permanent;

import java.util.List;

import io.naryo.application.event.store.EventStore;
import io.naryo.domain.configuration.eventstore.EventStoreConfiguration;
import io.naryo.domain.event.Event;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class EventStoreBroadcasterPermanentTrigger implements PermanentTrigger<Event> {

    private final List<EventStore<Event, EventStoreConfiguration>> eventStores;
    private final EventStoreConfiguration configuration;
    private Consumer<Event> consumer;

    public <C extends EventStoreConfiguration> EventStoreBroadcasterPermanentTrigger(
            List<EventStore<Event, EventStoreConfiguration>> eventStores, C configuration) {
        this.eventStores = eventStores;
        this.configuration = configuration;
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
                                eventStore.save(event, configuration);
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
