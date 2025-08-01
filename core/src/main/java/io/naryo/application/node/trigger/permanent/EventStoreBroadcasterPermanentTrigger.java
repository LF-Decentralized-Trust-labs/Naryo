package io.naryo.application.node.trigger.permanent;

import java.util.List;
import java.util.Set;

import io.naryo.application.event.store.EventStore;
import io.naryo.domain.configuration.eventstore.EventStoreConfiguration;
import io.naryo.domain.event.Event;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class EventStoreBroadcasterPermanentTrigger implements PermanentTrigger<Event> {

    private final Set<EventStore<? extends Event, ? extends EventStoreConfiguration>> eventStores;
    private final List<EventStoreConfiguration> configurations;
    private Consumer<Event> consumer;

    public <C extends EventStoreConfiguration> EventStoreBroadcasterPermanentTrigger(
            Set<EventStore<? extends Event, ? extends EventStoreConfiguration>> eventStores,
            List<EventStoreConfiguration> configurations) {
        this.eventStores = eventStores;
        this.configurations = configurations;
    }

    @Override
    public void onExecute(Consumer<Event> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void trigger(Event event) {
        EventStoreConfiguration eventStoreConfiguration = findConfigurationForEvent(event);
        this.eventStores.stream()
                .filter(eventStore -> eventStore.supports(event, eventStoreConfiguration))
                .forEach(
                        eventStore -> {
                            try {
                                @SuppressWarnings("unchecked")
                                EventStore<Event, EventStoreConfiguration> typedStore =
                                        (EventStore<Event, EventStoreConfiguration>) eventStore;
                                typedStore.save(event, eventStoreConfiguration);
                            } catch (Exception e) {
                                log.error(
                                        "Error while saving event {} to event store: {}",
                                        event.getEventType(),
                                        e.getMessage(),
                                        e);
                            }
                        });

        if (consumer != null) {
            try {
                consumer.accept(event);
            } catch (Exception e) {
                log.error(
                        "Error while consumer execution for event {}: {}",
                        event.getEventType(),
                        e.getMessage(),
                        e);
            }
        }
    }

    @Override
    public boolean supports(Event event) {
        return true;
    }

    private EventStoreConfiguration findConfigurationForEvent(Event event) {
        return configurations.stream()
                .filter(configuration -> configuration.getNodeId().equals(event.getNodeId()))
                .findFirst()
                .orElseThrow(
                        () ->
                                new IllegalStateException(
                                        "No configuration found for node: " + event.getNodeId()));
    }
}
